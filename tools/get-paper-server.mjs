// Downloads the latest Paper 1.21.11 server jar into ./test-server/paper.jar
// Uses the current fill.papermc.io v3 API (the old api.papermc.io v2 API returns 410).
import { mkdirSync, writeFileSync } from "node:fs";
import { join } from "node:path";

const MC = process.env.MC_VERSION ?? "1.21.11";
const dir = join(process.cwd(), "test-server");
mkdirSync(dir, { recursive: true });

const builds = await (await fetch(`https://fill.papermc.io/v3/projects/paper/versions/${MC}/builds`)).json();
const build = builds
    .filter((b) => b.downloads["server:default"])
    .sort((a, b) => b.id - a.id)
    .at(0);
if (!build) throw new Error(`No downloadable Paper build for ${MC}`);

const download = build.downloads["server:default"];
console.log(`Paper ${MC} build ${build.id} (${build.channel}) -> ${download.name}`);
const response = await fetch(download.url);
if (!response.ok) throw new Error(`HTTP ${response.status} for ${download.url}`);
const bytes = Buffer.from(await response.arrayBuffer());
writeFileSync(join(dir, "paper.jar"), bytes);
console.log(`saved test-server/paper.jar (${(bytes.length / 1024 / 1024).toFixed(1)} MB)`);
