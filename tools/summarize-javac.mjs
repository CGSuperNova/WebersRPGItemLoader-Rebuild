// Summarise a javac log: per-file counts and distinct error messages.
import { readFileSync } from "node:fs";
const log = readFileSync(process.argv[2] ?? "javac-loader.log", "utf8").replace(/\r/g, "");
const lines = log.split("\n");
const errors = [];
for (let i = 0; i < lines.length; i++) {
  const m = lines[i].match(/^(.*\.java):(\d+): (error|warning): (.*)$/);
  if (!m) continue;
  const detail = [];
  for (let j = i + 1; j < Math.min(i + 6, lines.length); j++) {
    if (/^\S.*\.java:\d+:|^\d+ (error|warning)/.test(lines[j])) break;
    if (lines[j].trim()) detail.push(lines[j].trim());
  }
  errors.push({ file: m[1].replace(/^.*[\\/]src[\\/]main[\\/]java[\\/]/, ""), line: +m[2], kind: m[3], msg: m[4], detail });
}
const isErr = errors.filter((e) => e.kind === "error");
console.log(`errors=${isErr.length}`);
const perFile = new Map();
for (const e of isErr) perFile.set(e.file, (perFile.get(e.file) ?? 0) + 1);
console.log("\n--- per file ---");
for (const [f, n] of [...perFile].sort((a, b) => b[1] - a[1])) console.log(String(n).padStart(4), f);
const groups = new Map();
for (const e of isErr) {
  const key = `${e.msg} | ${e.detail[0] ?? ""}`;
  if (!groups.has(key)) groups.set(key, []);
  groups.get(key).push(e);
}
console.log("\n--- distinct errors ---");
for (const [k, list] of [...groups].sort((a, b) => b[1].length - a[1].length)) {
  console.log(`\n[${list.length}x] ${k}`);
  for (const e of list.slice(0, 4)) console.log(`    ${e.file}:${e.line}`);
}
