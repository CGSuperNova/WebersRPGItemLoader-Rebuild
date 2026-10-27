// Mechanical cleanup of JD-Core output:
//  - strips the "/*  123 */ " line-number prefix JD-Core writes in front of every line
//  - drops the empty "/*     */" padding lines JD-Core inserts
//  - drops the trailing decompiler footer (Location / compiler version / JD-Core)
//  - normalises line endings to LF
// This only affects presentation, never the code itself.
import { readdirSync, readFileSync, writeFileSync, mkdirSync, existsSync } from "node:fs";
import { join, relative, dirname } from "node:path";

const root = process.cwd();
const modules = [
  { from: "rpgItemLoader1.6.9Beta.jar.src", to: "rpgitem-loader/src/main/java" },
  { from: "webersBuff.jar.src", to: "webers-buff/src/main/java" },
];

const PREFIX = /^\/\*\s*\d*\s*\*\/\s?/;
const EMPTY = /^\/\*\s*\*\/\s*$/;
const FOOTER_START = /^\/\*\s*Location:/;

function walk(dir, out = []) {
  for (const e of readdirSync(dir, { withFileTypes: true })) {
    const p = join(dir, e.name);
    if (e.isDirectory()) walk(p, out);
    else if (e.name.endsWith(".java")) out.push(p);
  }
  return out;
}

let total = { files: 0, before: 0, after: 0 };
for (const m of modules) {
  const srcRoot = join(root, m.from);
  const outRoot = join(root, m.to);
  for (const file of walk(srcRoot)) {
    const raw = readFileSync(file, "utf8").replace(/\r\n/g, "\n");
    const lines = raw.split("\n");
    const kept = [];
    for (const line of lines) {
      // The decompiler footer starts with "/* Location: ..." and runs to EOF.
      if (FOOTER_START.test(line.trim())) break;
      if (EMPTY.test(line.trim())) continue;
      kept.push(line.replace(PREFIX, ""));
    }
    // JD-Core renders ALOAD 0 / POP sequences as a bare `this;` statement.
    // It is never valid Java, so drop it (and any duplicated `this; this;`.
    let text = kept.join("\n").replace(/\bthis;\s*(?=this;)/g, "");
    text = text.replace(/\bthis;[ \t]*(?=[A-Za-z_$("'0-9])/g, "");
    text = text.replace(/^[ \t]*this;[ \t]*$/gm, "");
    kept.length = 0;
    kept.push(...text.split("\n"));
    while (kept.length && kept[kept.length - 1].trim() === "") kept.pop();
    const outFile = join(outRoot, relative(srcRoot, file));
    mkdirSync(dirname(outFile), { recursive: true });
    writeFileSync(outFile, kept.join("\n") + "\n", "utf8");
    total.files++;
    total.before += lines.length;
    total.after += kept.length;
  }
}
console.log(`cleaned ${total.files} files: ${total.before} -> ${total.after} lines`);
