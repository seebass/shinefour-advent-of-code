import { readText } from "./utils/read-text.js";
import { count, sum } from "ramda";

const text = readText("02");

function createLines() {
  return text
    .split("\n")
    .map((line) => line.split(" ").map((num) => Number.parseInt(num)));
}

function isReportSafe(numArray) {
  let isAscending;
  let isDescending;
  numArray.forEach((num, i) => {
    if (i === 0) {
      return;
    }
    const prev = numArray[i - 1];
    if (num > prev && num - prev <= 3 && isAscending !== false) {
      isAscending = true;
      isDescending = false;
    } else if (num < prev && prev - num <= 3 && isDescending !== false) {
      isAscending = false;
      isDescending = true;
    } else {
      isAscending = false;
      isDescending = false;
    }
  });
  return isAscending || isDescending;
}

function a() {
  const reports = createLines();
  return count(isReportSafe, reports);
}

/**
 *
 * @param report
 * @returns {Generator<number[], undefined>}
 */
function* variationGenerator(report) {
  yield report;
  for (let i = 0; i < report.length; i++) {
    yield report.toSpliced(i, 1);
  }
}

function b() {
  const reports = createLines();
  const results = reports.map((report) => {
    const variations = variationGenerator(report);
    for (const variation of variations) {
      if (isReportSafe(variation)) {
        return true;
      }
    }
    return false;
  });
  return sum(results);
}

export const day02 = { a, b };
