import { zip } from "ramda";
import { readFileSync } from "node:fs";

export function readText(demo = false) {
  return readFileSync(`./${demo ? "demo" : "challenge"}.txt`, {
    encoding: "utf8",
  });
}

const text = readText();

function createGroups() {
  const lines = text
    .split("\n")
    .map((line) => line.split("   ").map((num) => Number.parseInt(num)));
  const firstGroup = lines.map((line) => line.at(0));
  const secondGroup = lines.map((line) => line.at(1));
  return [firstGroup, secondGroup];
}

function partOne() {
  const [firstGroup, secondGroup] = createGroups();
  return zip(firstGroup.toSorted(), secondGroup.toSorted()).reduce(
    (acc, [a, b]) => acc + Math.abs(a - b),
    0,
  );
}

function partTwo() {
  const [firstGroup, secondGroup] = createGroups();
  const counts = firstGroup.reduce(
    (acc, curr) =>
      acc.get(curr) ??
      acc.set(curr, secondGroup.filter((num) => num === curr).length),
    new Map(),
  );
  return firstGroup.reduce((acc, curr) => {
    return acc + curr * counts.get(curr);
  }, 0);
}

console.log("Part One", partOne());
console.log("Part Two", partTwo());
