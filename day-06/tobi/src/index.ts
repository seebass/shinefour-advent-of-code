import * as fs from 'fs';

const input = fs.readFileSync('./src/input.txt').toString().split('\n').filter((row) => row.trim() !== '').map((row) => row.split(""))

// part 1

const xMax = input[0].length;
const yMax = input.length;

interface Position {
    x: number;
    y: number;
}

type Direction = 'UP' | 'DOWN' | 'LEFT' | 'RIGHT'

console.log('part 1 result', countMarks(executeRoute()))

function getCurrentPosition(map: string[][]): Position | null {
    for (let y = 0; y < yMax; y++) {
        const row = map[y];
        for (let x = 0; x < xMax; x++) {
            const value = row[x]
            if (value === '^') {
                return {x, y}
            }
        }
    }
    return null
}

function getNextPosition(map: string[][], currentPosition: Position, direction: Direction): Position | null {
    let result = getNextPositionInDirection(currentPosition, direction)
    if (result.x < 0 || result.x >= xMax || result.y < 0 || result.y >= yMax) {
        return null
    }
    if (map[result.y][result.x] === '#') {
        direction = direction === 'UP' ? 'RIGHT' : direction === 'RIGHT' ? 'DOWN' : direction === 'DOWN' ? 'LEFT' : 'UP'
        result = getNextPositionInDirection(currentPosition, direction)
    }
    if (result.x < 0 || result.x >= xMax || result.y < 0 || result.y >= yMax) {
        return null
    }
    return result
}

function getNextPositionInDirection(currentPosition: Position, direction: Direction): Position {
    if (direction === 'UP') {
        return {x: currentPosition.x, y: currentPosition.y - 1}
    }
    if (direction === 'DOWN') {
        return {x: currentPosition.x, y: currentPosition.y + 1}
    }
    if (direction === 'LEFT') {
        return {x: currentPosition.x - 1, y: currentPosition.y}
    }
    return {x: currentPosition.x + 1, y: currentPosition.y}
}

function markCurrentPosition(map: string[][], currentPosition: Position) {
    map[currentPosition.y][currentPosition.x] = 'X'
}

function getNewDirection(currentPosition: Position, nextPosition: Position): Direction {
    if (currentPosition.x !== nextPosition.x) {
        return currentPosition.x < nextPosition.x ? 'RIGHT' : 'LEFT'
    }
    return currentPosition.y < nextPosition.y ? 'DOWN' : 'UP'
}

function countMarks(map: string[][]) {
    return map.reduce((total, row) => {
        return total + row.reduce((rowTotal, value) => {
            return rowTotal + (value === 'X' ? 1 : 0);
        }, 0);
    }, 0);
}

function executeRoute() {
    let direction: Direction = 'UP'
    let resultMap = input.map(row => [...row]);
    let currentPosition = getCurrentPosition(resultMap);
    let nextPosition = null;
    if (currentPosition) {
        markCurrentPosition(resultMap, currentPosition)
        nextPosition = getNextPosition(resultMap, currentPosition, direction);
        if (nextPosition) {
            direction = getNewDirection(currentPosition, nextPosition);
        }
    }
    while (nextPosition) {
        currentPosition = nextPosition;
        markCurrentPosition(resultMap, currentPosition)
        nextPosition = getNextPosition(resultMap, currentPosition, direction);
        if (nextPosition) {
            direction = getNewDirection(currentPosition, nextPosition);
        }
    }
    return resultMap
}

// part 2


console.log('part 2 result', countLoops())

function countLoops() {
    const startingPosition = getCurrentPosition(input)
    const executedMap = executeRoute()
    executedMap[startingPosition!.y][startingPosition!.x] = '^'
    let result = 0;

    for (let y = 0; y < yMax; y++) {
        const row = executedMap[y];
        for (let x = 0; x < xMax; x++) {
            const value = row[x]
            if (value === 'X') {
                const modifiedMap = executedMap.map(row => [...row]);
                modifiedMap[y][x] = '#';
                console.log(y, x);
                if (isLoop(modifiedMap, startingPosition!)) {
                    result += 1
                }
            }
        }
    }
    return result
}

function isLoop(modifiedMap: string[][], currentPosition: Position) {
    const seenPositions: string[] = []
    let direction: Direction = 'UP'
    let nextPosition = getNextPosition(modifiedMap, currentPosition, direction);
    if (!nextPosition) {
        return false
    }
    direction = getNewDirection(currentPosition, nextPosition);
    seenPositions.push(JSON.stringify(nextPosition) + direction)
    while (nextPosition) {
        currentPosition = nextPosition;
        nextPosition = getNextPosition(modifiedMap, currentPosition, direction);
        if (!nextPosition) {
            return false
        }
        direction = getNewDirection(currentPosition, nextPosition);
        if (seenPositions.includes(JSON.stringify(nextPosition) + direction)) {
            return true;
        }
        seenPositions.push(JSON.stringify(nextPosition) + direction)
    }
    return false
}
