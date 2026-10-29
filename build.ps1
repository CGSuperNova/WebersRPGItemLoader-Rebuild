<#
.SYNOPSIS
    Builds both WebersRPGItem plugins without requiring Maven.

.DESCRIPTION
    Compiles rpgitem-loader and webers-buff against the Paper 1.21.11 API and writes the
    plugin jars to .\dist.

    The compile classpath is taken from .\libs. If that folder is empty the script asks Node
    (tools/fetch-deps.mjs) to download paper-api and the Adventure API from the PaperMC
    repository. Maven users can run `mvn package` instead; both paths produce the same jars.

.EXAMPLE
    .\build.ps1
    .\build.ps1 -Clean
#>
param(
    [switch]$Clean
)

$ErrorActionPreference = 'Stop'
$root = Split-Path -Parent $MyInvocation.MyCommand.Definition
Set-Location $root

$version = '1.9.0'
$modules = @(
    @{ Name = 'rpgitem-loader'; Jar = "WeberRpgitemLoader-$version.jar"; DependsOnLoader = $false },
    @{ Name = 'webers-buff';    Jar = "WebersBuff-$version.jar";         DependsOnLoader = $true  }
)

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

function Ensure-Dependencies {
    $paper = Get-ChildItem 'libs' -Filter 'paper-api-*.jar' -ErrorAction SilentlyContinue
    if ($paper) { return }
    if (-not (Get-Command node -ErrorAction SilentlyContinue)) {
        throw '.\libs is empty and Node.js is not installed. Install Node or run `mvn package` instead.'
    }
    Write-Host 'Downloading Paper 1.21.11 API ...' -ForegroundColor Cyan
    node tools/fetch-deps.mjs io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT | Out-Host
    foreach ($artifact in @(
            'net.kyori:adventure-api:4.26.1',
            'net.kyori:adventure-key:4.26.1',
            'net.kyori:examination-api:1.3.0',
            'net.kyori:adventure-text-serializer-legacy:4.26.1',
            'net.kyori:adventure-text-serializer-plain:4.26.1')) {
        node tools/fetch-deps.mjs $artifact | Out-Host
    }
}

function Build-Module {
    param([string]$Name, [string]$JarName, [string]$ExtraClasspath)

    $sourceDir = Join-Path $root "$Name\src\main\java"
    $resourceDir = Join-Path $root "$Name\src\main\resources"
    $targetDir = Join-Path $root "$Name\target"
    $classesDir = Join-Path $targetDir 'classes'

    Write-Host "== $Name ==" -ForegroundColor Cyan
    if ($Clean -and (Test-Path $targetDir)) { Remove-Item $targetDir -Recurse -Force }
    if (Test-Path $classesDir) { Remove-Item $classesDir -Recurse -Force }
    New-Item -ItemType Directory -Force $classesDir | Out-Null

    $classpath = (Get-ChildItem (Join-Path $root 'libs') -Filter *.jar | ForEach-Object { $_.FullName }) -join ';'
    if ($ExtraClasspath) { $classpath = "$ExtraClasspath;$classpath" }

    $sources = Get-ChildItem $sourceDir -Recurse -Filter *.java | ForEach-Object { $_.FullName.Replace('\', '/') }
    if (-not $sources) { throw "No sources found in $sourceDir" }

    $argFile = Join-Path $targetDir 'javac-args.txt'
    $argLines = @(
        '--release', '21',
        '-encoding', 'UTF-8',
        '-proc:none',
        '-Xlint:-options',
        '-d', $classesDir.Replace('\', '/'),
        '-cp', ('"' + ($classpath.Replace('\', '/')) + '"')
    ) + $sources
    Set-Content -Path $argFile -Value $argLines -Encoding ASCII

    & $script:javac "@$argFile"
    if ($LASTEXITCODE -ne 0) { throw "Compilation failed for $Name" }

    Copy-Item (Join-Path $resourceDir '*') $classesDir -Force -Recurse
    if (-not (Test-Path (Join-Path $classesDir 'plugin.yml'))) { throw "plugin.yml missing in $Name" }

    $distDir = Join-Path $root 'dist'
    New-Item -ItemType Directory -Force $distDir | Out-Null
    $jarPath = Join-Path $distDir $JarName
    if (Test-Path $jarPath) { Remove-Item $jarPath -Force }
    & $script:jarTool --create --file $jarPath -C $classesDir .
    if ($LASTEXITCODE -ne 0) { throw "Packaging failed for $Name" }
    Write-Host ("   -> dist\{0} ({1:N0} KB)" -f $JarName, ((Get-Item $jarPath).Length / 1KB)) -ForegroundColor Green
    return $classesDir
}

$script:jdk = Resolve-Jdk
$script:javac = Join-Path $script:jdk 'bin\javac.exe'
$script:jarTool = Join-Path $script:jdk 'bin\jar.exe'
Write-Host "JDK: $script:jdk" -ForegroundColor DarkGray

Ensure-Dependencies
$loaderClasses = Build-Module -Name 'rpgitem-loader' -JarName $modules[0].Jar -ExtraClasspath ''
Build-Module -Name 'webers-buff' -JarName $modules[1].Jar -ExtraClasspath $loaderClasses | Out-Null

Write-Host 'Build completed.' -ForegroundColor Green
