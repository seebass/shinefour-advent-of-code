import { getPuzzle, splitStringByLines } from "../utils";

type MapCell = { char: string, antinode: boolean };
type Map = Array<Array<MapCell>>;
type Position = { x: number, y: number };

const data = await getPuzzle(2024, 8);

function getMap(data: string) {
    return splitStringByLines(data).map(line => line.split('').map((char) => ({ char, antinode: false })));
}

function getMapSize(data: Map) {
    if (!data[0]) {
        throw new Error("Error parsing Map");
    }
    return {
        x: data[0].length,
        y: data.length,
    }
}

function getAntennasByFrequency(map: Map, size: Position) {
    const antennasByFrequency = new Map<string, Array<Position>>();

    for (let y = 0; y < size.y; y++) {
        for (let x = 0; x < size.x; x++) {
            const char = map[y]![x]!.char;
            if (char !== '.') {
                if (!antennasByFrequency.has(char)) {
                    antennasByFrequency.set(char, []);
                }
                antennasByFrequency.get(char)!.push({ x, y });
            }
        }
    }
    return antennasByFrequency;
}

const puzzle = getMap(data);
const size = getMapSize(puzzle);
const antennasByFrequency = getAntennasByFrequency(puzzle, size);

function solvePuzzle(setAntinodesFunction: (puzzleMap: Map, diff: Position, antenna1: Position, antenna2: Position) => void) {
    const puzzleMap = [...puzzle];

    for (const antennaLocations of antennasByFrequency.values()) {
        if (antennaLocations.length < 2) {
            continue;
        }

        for (let i = 0; i < antennaLocations.length; i++) {
            for (let j = i + 1; j < antennaLocations.length; j++) {
                const antenna1 = antennaLocations[i]!;
                const antenna2 = antennaLocations[j]!;
                const diff = {
                    y: antenna2.y - antenna1.y,
                    x: antenna2.x - antenna1.x
                };

                setAntinodesFunction(puzzleMap, diff, antenna1, antenna2);
            }
        }
    }

    return puzzleMap.reduce((acc, line) => acc + line.filter(pos => pos.antinode).length, 0);
}

function puzzle1AntinodeSetter(puzzleMap: Map, diff: Position, antenna1: Position, antenna2: Position) {
    const antinodeY1 = antenna1.y - diff.y;
    const antinodeX1 = antenna1.x - diff.x;

    const antinodeY2 = antenna2.y + diff.y;
    const antinodeX2 = antenna2.x + diff.x;

    if (antinodeY1 >= 0 && antinodeY1 < size.y && antinodeX1 >= 0 && antinodeX1 < size.x) {
        puzzleMap[antinodeY1]![antinodeX1]!.antinode = true;
    }
    if (antinodeY2 >= 0 && antinodeY2 < size.y && antinodeX2 >= 0 && antinodeX2 < size.x) {
        puzzleMap[antinodeY2]![antinodeX2]!.antinode = true;
    }
}

function puzzle2AntinodeSetter(puzzleMap: Map, diff: Position, antenna1: Position) {
    let antinodeY1 = antenna1.y;
    let antinodeX1 = antenna1.x;
    while (antinodeY1 >= 0 && antinodeY1 < size.y && antinodeX1 >= 0 && antinodeX1 < size.x) {
        puzzleMap[antinodeY1]![antinodeX1]!.antinode = true;
        antinodeY1 -= diff.y;
        antinodeX1 -= diff.x;
    }

    let antinodeY2 = antenna1.y + diff.y;
    let antinodeX2 = antenna1.x + diff.x;
    while (antinodeY2 >= 0 && antinodeY2 < size.y && antinodeX2 >= 0 && antinodeX2 < size.x) {
        puzzleMap[antinodeY2]![antinodeX2]!.antinode = true;
        antinodeY2 += diff.y;
        antinodeX2 += diff.x;
    }
}

console.log("Puzzle1: ", solvePuzzle(puzzle1AntinodeSetter));
console.log("Puzzle2: ", solvePuzzle(puzzle2AntinodeSetter));