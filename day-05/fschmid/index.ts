import { sum } from "ramda";

export function readText(demo = false) {
  const decoder = new TextDecoder("utf-8");
  const text = Deno.readFileSync(`./${demo ? "demo" : "challenge"}.txt`);
  return decoder.decode(text);
}

const text = readText();

function getRulesAndUpdates(
  text: string,
): { rules: [number, number][]; updates: number[][] } {
  const lines = text.split("\n");
  const separatorLineIndex = lines.findIndex((line) => line.length === 0);

  const rules = lines.slice(0, separatorLineIndex).map((rule) =>
    rule.split("|").map((s) => Number.parseInt(s))
  ) as [number, number][];
  const updates = lines.slice(separatorLineIndex + 1).map((line) =>
    line.split(",").map((s) => Number.parseInt(s))
  );

  return {
    rules,
    updates,
  };
}

function eliminateIrrelevantRules(rules: [number, number][], update: number[]) {
  return rules.filter((rule) => rule.every((value) => update.includes(value)));
}

function findStarter(rules: [number, number][], update: number[]) {
  const starters = update.filter((value) =>
    !rules.map((r) => r[1]).includes(value)
  );
  if (starters.length !== 1) {
    throw new Error(
      `Could not determine distinct starter. ${starters.length} starters found.`,
    );
  }
  return starters[0];
}

function updateWithoutStarter(update: number[], starter: number) {
  return update.toSpliced(update.indexOf(starter), 1);
}

function sortUpdate(rules: [number, number][], update: number[]): number[] {
  rules = eliminateIrrelevantRules(rules, update);
  const starter = findStarter(rules, update);
  update = updateWithoutStarter(update, starter);
  if (update.length === 0) {
    return [starter];
  }
  return [starter, ...sortUpdate(rules, update)];
}

function getMiddleElement(list: number[]) {
  const index = Math.floor(list.length / 2);
  return list[index];
}

function partOne() {
  const { rules, updates } = getRulesAndUpdates(text);
  return sum(
    updates.filter((update) =>
      sortUpdate(rules, update).every((v, i) => v === update[i])
    ).map(getMiddleElement),
  );
}

function partTwo() {
  const { rules, updates } = getRulesAndUpdates(text);
  return sum(
    updates.filter((update) =>
      !sortUpdate(rules, update).every((v, i) => v === update[i])
    ).map((update) => sortUpdate(rules, update)).map(getMiddleElement),
  );
}

console.log("Part One", partOne());
console.log("Part Two", partTwo());
