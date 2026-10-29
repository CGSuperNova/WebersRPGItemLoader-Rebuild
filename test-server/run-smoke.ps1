<#
.SYNOPSIS
    Boots a throw-away Paper 1.21.11 server with both plugins and an in-server smoke test.

.DESCRIPTION
    1. downloads test-server/paper.jar if missing (Node.js required);
    2. rebuilds both plugins with build.ps1;
    3. compiles test-server/smoke/WeberSmokeTest and installs all three jars;
    4. starts the server. The smoke plugin runs 13 assertions and then shuts the server down.

    Results are printed by the smoke plugin with the "SMOKE " prefix and are also written to
    test-server/logs/latest.log.

.EXAMPLE
    .\test-server\run-smoke.ps1
#>
param(
    [switch]$SkipDownload
)

$ErrorActionPreference = 'Stop'
$root = Split-Path -Parent (Split-Path -Parent $MyInvocation.MyCommand.Definition)
Set-Location $root

$serverDir = Join-Path $root 'test-server'

function Resolve-Jdk {
    $candidates = @()
    if ($env:JAVA_HOME) { $candidates += $env:JAVA_HOME }
    $candidates += 'D:\jdk_collection\jdk-21.0.3+9'
    $candidates += (Get-ChildItem 'C:\Program Files\Java','C:\Program Files\Eclipse Adoptium','C:\Program Files\Microsoft' -Directory -ErrorAction SilentlyContinue | ForEach-Object { $_.FullName })
    foreach ($candidate in $candidates) {
        $javac = Join-Path $candidate 'bin\javac.exe'
        if (Test-Path $javac) {
            $release = & $javac -version 2>&1
            if ($release -match 'javac (2[1-9]|[3-9]\d)') { return $candidate }
        }
    }
    throw 'No JDK 21+ found. Set JAVA_HOME to a JDK 21 installation.'
}

$jdk = Resolve-Jdk
$java = Join-Path $jdk 'bin\java.exe'
$javac = Join-Path $jdk 'bin\javac.exe'

if (-not $SkipDownload -and -not (Test-Path (Join-Path $serverDir 'paper.jar'))) {
    node tools/get-paper-server.mjs
}

& (Join-Path $root 'build.ps1')

$classpath = @(
    (Get-ChildItem (Join-Path $root 'libs') -Filter *.jar | ForEach-Object { $_.FullName })
    (Join-Path $root 'rpgitem-loader\target\classes')
    (Join-Path $root 'webers-buff\target\classes')
) -join ';'

$smokeClasses = Join-Path $serverDir 'smoke-classes'
if (Test-Path $smokeClasses) { Remove-Item $smokeClasses -Recurse -Force }
New-Item -ItemType Directory -Force $smokeClasses | Out-Null
& $javac --release 21 -encoding UTF-8 -proc:none -nowarn -d $smokeClasses -cp $classpath (Join-Path $serverDir 'smoke\smoke\WeberSmokeTest.java')
if ($LASTEXITCODE -ne 0) { throw 'smoke test plugin failed to compile' }
Copy-Item (Join-Path $serverDir 'smoke\plugin.yml') $smokeClasses -Force

$pluginsDir = Join-Path $serverDir 'plugins'
New-Item -ItemType Directory -Force $pluginsDir | Out-Null
Copy-Item (Join-Path $root 'dist\WeberRpgitemLoader-1.9.0.jar') $pluginsDir -Force
Copy-Item (Join-Path $root 'dist\WebersBuff-1.9.0.jar') $pluginsDir -Force
& (Join-Path (Split-Path $java) 'jar.exe') --create --file (Join-Path $pluginsDir 'WeberSmokeTest.jar') -C $smokeClasses .
if ($LASTEXITCODE -ne 0) { throw 'smoke test jar failed to package' }

Set-Content (Join-Path $serverDir 'eula.txt') 'eula=true' -Encoding ASCII
if (-not (Test-Path (Join-Path $serverDir 'server.properties'))) {
    @'
online-mode=false
level-type=minecraft\:flat
generate-structures=false
spawn-protection=0
max-players=5
view-distance=4
simulation-distance=4
enable-command-block=false
'@ | Set-Content (Join-Path $serverDir 'server.properties') -Encoding ASCII
}

Push-Location $serverDir
try {
    # Direct invocation keeps the server console in this script's output. The smoke plugin
    # schedules its own shutdown after the assertions (plus a 2 minute failsafe).
    & $java -Xms2G -Xmx2G -XX:+UseG1GC -jar paper.jar --nogui
} finally {
    Pop-Location
}
