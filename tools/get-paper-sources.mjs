// Downloads the Paper API sources jar into tools/ for reference (not shipped).
import { writeFileSync } from "node:fs";
const base = "https://repo.papermc.io/repository/maven-public/io/papermc/paper/paper-api/1.21.11-R0.1-SNAPSHOT/";
const name = "paper-api-1.21.11-R0.1-20260511.115010-91-sources.jar";
const r = await fetch(base + name);
if (!r.ok) throw new Error(`HTTP ${r.status}`);
const bytes = Buffer.from(await r.arrayBuffer());
writeFileSync("tools/paper-api-sources.jar", bytes);
console.log(`saved tools/paper-api-sources.jar (${(bytes.length / 1024).toFixed(0)} KB)`);
