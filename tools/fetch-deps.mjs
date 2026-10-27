// Minimal Maven dependency fetcher (no Maven needed).
// Resolves one artifact plus its transitive compile/runtime dependencies from the
// PaperMC public repository (which proxies Maven Central) into ./libs.
// Usage: node tools/fetch-deps.mjs io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT
import { mkdirSync, writeFileSync, existsSync } from "node:fs";
import { join } from "node:path";

const REPO = "https://repo.papermc.io/repository/maven-public/";
const LIBS = join(process.cwd(), "libs");
mkdirSync(LIBS, { recursive: true });

const seen = new Set();
const jars = [];

async function get(url, as = "text") {
  let last;
  for (let i = 0; i < 3; i++) {
    const r = await fetch(url);
    if (r.ok) return as === "buf" ? Buffer.from(await r.arrayBuffer()) : await r.text();
    last = new Error(`HTTP ${r.status} ${url}`);
    await new Promise((res) => setTimeout(res, 1200));
  }
  throw last;
}

function pathOf(g, a, dirVersion, fileVersion, ext) {
  return `${g.replace(/\./g, "/")}/${a}/${dirVersion}/${a}-${fileVersion}.${ext}`;
}

async function snapshotVersion(g, a, v) {
  if (!v.endsWith("-SNAPSHOT")) return v;
  const base = v.slice(0, -"-SNAPSHOT".length);
  const xml = await get(`${REPO}${g.replace(/\./g, "/")}/${a}/${v}/maven-metadata.xml`);
  const sn = xml.match(/<snapshot>([\s\S]*?)<\/snapshot>/);
  if (sn) {
    const ts = sn[1].match(/<timestamp>([^<]+)/)?.[1];
    const bn = sn[1].match(/<buildNumber>([^<]+)/)?.[1];
    if (ts && bn) return `${base}-${ts}-${bn}`;
  }
  return base; // fall back to the un-timestamped file
}

async function resolve(g, a, v, depth = 0) {
  const key = `${g}:${a}:${v}`;
  if (seen.has(key)) return;
  seen.add(key);
  const pad = "  ".repeat(depth);
  let resolved = v;
  try {
    resolved = await snapshotVersion(g, a, v);
  } catch (e) {
    console.warn(`${pad}! metadata ${key}: ${e.message}`);
  }
  const jarUrl = `${REPO}${pathOf(g, a, v, resolved, "jar")}`;
  try {
    const buf = await get(jarUrl, "buf");    const file = join(LIBS, `${a}-${v.replace(/-SNAPSHOT$/, "")}.jar`);
    if (!existsSync(file)) writeFileSync(file, buf);
    jars.push(file);
    console.log(`${pad}+ ${a}-${v} (${(buf.length / 1024).toFixed(0)} KB)`);
  } catch (e) {
    console.warn(`${pad}- jar ${key}: ${e.message}`);
    console.warn(`${pad}  url=${JSON.stringify(jarUrl)}`);
  }
  // transitive deps
  let pom;
  try {
    pom = await get(`${REPO}${pathOf(g, a, v, resolved, "pom")}`);
  } catch (e) {
    console.warn(`${pad}! pom ${key}: ${e.message}`);
    return;
  }
  const deps = [...pom.matchAll(/<dependency>([\s\S]*?)<\/dependency>/g)].map((m) => m[1]);
  for (const d of deps) {
    const gg = d.match(/<groupId>([^<]+)/)?.[1];
    const aa = d.match(/<artifactId>([^<]+)/)?.[1];
    const vv = d.match(/<version>([^<]+)/)?.[1];
    const scope = d.match(/<scope>([^<]+)/)?.[1] ?? "compile";
    const optional = d.match(/<optional>([^<]+)/)?.[1] === "true";
    if (!gg || !aa || !vv) continue;
    if (optional || ["test", "provided", "system", "import"].includes(scope)) continue;
    if (vv.includes("${")) continue; // property placeholders we cannot resolve
    await resolve(gg, aa, vv, depth + 1);
  }
}

const [g, a, v] = (process.argv[2] ?? "").split(":");
if (!g) {
  console.error("usage: node tools/fetch-deps.mjs group:artifact:version");
  process.exit(1);
}
await resolve(g, a, v);
console.log(`\n${jars.length} jars in ./libs`);
