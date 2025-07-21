import { getPuzzle, splitStringByLines } from "../utils";

const puzzle = await getPuzzle(2024, 10);

const map = splitStringByLines(puzzle).map(line => line.split('').map(char => Number(char)));

const trailings = new Set();
let count = 0;

function checkNextNumber(nr: number, x: number, y: number, initX: number, initY: number) {
    if (nr === 9) {
        trailings.add(`${initX}:${initY}-${x}:${y}`);
        count++;
        return;
    }

    const nextNumber = nr + 1;

    const nextX = x + 1;
    const right = nextX < map[y]!.length ? map[y]![nextX] : null;
    if (right === nextNumber) {
        checkNextNumber(right, nextX, y, initX, initY);
    }

    const previousX = x - 1;
    const left = previousX >= 0 ? map[y]![previousX] : null;
    if (left === nextNumber) {
        checkNextNumber(left, previousX, y, initX, initY);
    }

    const previousY = y - 1;
    const up = previousY >= 0 ? map[previousY]![x] : null;
    if (up === nextNumber) {
        checkNextNumber(up, x, previousY, initX, initY);
    }

    const nextY = y + 1;
    const down = nextY <  map.length ? map[nextY]![x] : null;
    if (down === nextNumber) {
        checkNextNumber(down, x, nextY, initX, initY);
    }
}

for (let y=0; y <= map.length - 1; y++) {
    const line = map[y];
    if (!line) {
        continue;
    }
    for (let x=0; x <= line.length - 1; x++) {
        const nr = line[x];
        if (nr === 0) {
            checkNextNumber(0, x, y, x, y);
        }
    }
}

console.log("Puzzle 1: ", trailings.size);
console.log("Puzzle 2: ", count);
