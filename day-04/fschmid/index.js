import { flatten, sum } from "ramda";
import { readFileSync } from "node:fs";

/**
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
 * @param line {string}
 * @returns {number}
 */
function countInLine(line) {
  let count = 0;
  let rest = line;
  let index = -1;
  do {
    index = rest.indexOf("XMAS");
    if (index !== -1) {
      count++;
      rest = rest.substring(index + 1);
    }
  } while (index !== -1);
  return count;
}

/**
 * @param lines {string[]}
 * @returns {number}
 */
function countAll(lines) {
  return sum(lines.map((line) => countInLine(line)));
}

/**
 * @param x {number}
 * @param y {number}
 * @param matrix {string[][]}
 * @returns {string[]}
 */
function getDiagonal(x, y, matrix) {
  if (x < matrix.length && y < matrix.at(0).length) {
    const char = matrix[x][y];
    return getDiagonal(++x, ++y, matrix).concat(char);
  }
  return [];
}

/**
 * @param matrix {string[][]}
 * @returns {string[][]}
 */
function getDiagonals(matrix) {
  let rowLength = matrix.length;
  let columnLength = matrix.at(0).length;

  const diags = [];
  for (let row = rowLength - 1; row > 0; row--) {
    diags.push(getDiagonal(row, 0, matrix));
  }
  for (let col = 0; col < columnLength; col++) {
    diags.push(getDiagonal(0, col, matrix));
  }
  return diags;
}

function partOne() {
  const matrix = text.split("\n").map((l) => l.split(""));
  const rows = matrix.map((l) => l.join(""));
  const rowsReversed = matrix.map((l) => l.toReversed().join(""));
  const columns = matrix
    .at(0)
    .map((a, i) => matrix.map((l) => l.at(i)).join(""));
  const verticalsReversed = matrix.at(0).map((a, i) =>
    matrix
      .map((l) => l.at(i))
      .toReversed()
      .join(""),
  );
  const diags1 = getDiagonals(matrix).map((l) => l.join(""));
  const diags1Reversed = getDiagonals(matrix).map((l) =>
    l.toReversed().join(""),
  );
  const diags2 = getDiagonals(matrix.map((l) => l.toReversed())).map((l) =>
    l.join(""),
  );
  const diags2Reversed = getDiagonals(matrix.map((l) => l.toReversed())).map(
    (l) => l.toReversed().join(""),
  );

  return sum(
    [
      rows,
      rowsReversed,
      columns,
      verticalsReversed,
      diags1,
      diags1Reversed,
      diags2,
      diags2Reversed,
    ].map(countAll),
  );
}

/**
 * @param row {number}
 * @param column {number}
 * @param matrix {string[][]}
 * @returns {boolean}
 */
function isAtBoarder(row, column, matrix) {
  return (
    row === 0 ||
    column === 0 ||
    row === matrix.length - 1 ||
    column === matrix.at(0).length - 1
  );
}

/**
 * @param matrix {string[][]}
 * @returns {({r: number, c: number}) => boolean}
 */
const validate =
  (matrix) =>
  ({ row, column }) => {
    if (isAtBoarder(row, column, matrix)) {
      return false;
    }

    const crossWords = [
      `${matrix.at(row - 1)?.at(column - 1)}A${matrix.at(row + 1)?.at(column + 1)}`,
      `${matrix.at(row - 1)?.at(column + 1)}A${matrix.at(row + 1)?.at(column - 1)}`,
    ];

    const validWords = ["SAM", "MAS"];
    return crossWords.every((word) => validWords.includes(word));
  };

function partTwo() {
  const matrix = text.split("\n").map((l) => l.split(""));
  const allAPositions = flatten(
    matrix.map((arr, row) =>
      arr
        .map((char, column) => ({ row, column, char }))
        .filter(({ char }) => char === "A"),
    ),
  );

  return sum(allAPositions.map(validate(matrix)));
}

console.log("Part One", partOne());
console.log("Part Two", partTwo());
