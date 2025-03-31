import { multiply, sum } from "ramda";
import { readFileSync } from "node:fs";

/**
 *
 * @param demo {boolean}
 * @returns {string}
 */
export function readText(demo = false) {
  return readFileSync(`./${demo ? "demo" : "challenge"}.txt`, {
    encoding: "utf8",
  });
}

const text = readText();

/**
 *
 * @param s {string}
 * @returns {string}
 */
function getNextExecutionPart(s) {
  const endIndex = s.indexOf("don't()");
  if (endIndex === -1) {
    return s;
  }
  return s.substring(0, endIndex);
}

/**
 *
 * @param s {string}
 * @returns {string}
 */
function findNextExecutionPart(s) {
  let i = s.indexOf("do()");
  if (i === -1) {
    return "";
  }
  return s.substring(i);
}

/**
 *
 * @param s {string}
 * @returns {{remainingScript: string, multiplicationGroups: [number, number][]}}
 */
function getNextMultiplicationGroups(s) {
  const executionPart = getNextExecutionPart(s);
  return {
    remainingScript: s.substring(executionPart.length),
    multiplicationGroups: getMultiplicationGroups(executionPart),
  };
}

/**
 *
 * @param s {string}
 * @returns {[number,number][]}
 */
function getMultiplicationGroups(s) {
  const matches = s.match(/mul\(\d{1,3},\d{1,3}\)/g);
  return matches.map((match) =>
    match.match(/\d+/g).map((i) => Number.parseInt(i)),
  );
}

/**
 *
 * @param groups {[number, number][]}
 * @returns {number}
 */
function multiplyAndSumGroups(groups) {
  return sum(groups.map(([a, b]) => multiply(a, b)));
}

function partOne() {
  return multiplyAndSumGroups(getMultiplicationGroups(text));
}

function partTwo() {
  let remainingText = text;
  let groups = [];
  do {
    const { remainingScript, multiplicationGroups } =
      getNextMultiplicationGroups(remainingText);
    groups.push(...multiplicationGroups);
    remainingText = findNextExecutionPart(remainingScript);
  } while (remainingText.length);
  return multiplyAndSumGroups(groups);
}

console.log("Part One", partOne());
console.log("Part Two", partTwo());
