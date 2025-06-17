import { getPuzzle, splitStringByLines } from "../utils";

const ObstacleChar = '#';
const Directions = {
    Up: '^',
    Down: 'v',
    Right: '>',
    Left: '<',
};

type Map = Array<Array<{ char: string, visited: boolean}>>;
type Position = { x: number, y: number };
type Guard = {
    direction: typeof Directions[keyof typeof Directions],
    position: Position
}

const data = await getPuzzle(2024, 6);

function getMap(data: string) {
    return splitStringByLines(data).map(line => line.split('').map((char) => ({ char, visited: false })));
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

function getInitGuard(data: Map): Guard {
    let x = 0;
    const startDir = Object.values(Directions).find(dir => data.some(row => row.some(cell => cell.char === dir)));
    let y = data.findIndex((line) => {
        const pos = line.findIndex(pos => startDir === pos.char);
        const guardPositionFounded = pos >= 0;
        if (guardPositionFounded) {
            x = pos;
        }
        return guardPositionFounded;
    });

    if (y === -1 || !startDir) throw new Error("Guard start position not found");

    return {
        direction: startDir,
        position: { x, y }
    };
}

const InitMap = getMap(data);
const InitGuard = getInitGuard(InitMap);
const size = getMapSize(InitMap);

function isInsideMap(pos: Position) {
    return pos.x >= 0 && pos.x < size.x && pos.y >= 0 && pos.y < size.y;
}

function move(map: Map, guard: Guard) {
    let { direction, position } = guard;
    if (direction === Directions.Up) {
        if (position.y > 0 && map[position.y - 1]![position.x]!.char === ObstacleChar) {
            direction = Directions.Right;
        } else {
            position.y--;
        }
    } else if (direction === Directions.Down) {
        if (position.y + 1 < size.y && map[position.y + 1]![position.x]!.char === ObstacleChar) {
            direction = Directions.Left;
        } else {
            position.y++;
        }
    } else if (direction === Directions.Right) {
        if (position.x + 1 < size.x && map[position.y]![position.x + 1]!.char === ObstacleChar) {
            direction = Directions.Down;
        } else {
            position.x++;
        }
    } else if (direction === Directions.Left) {
        if (position.x > 0 && map[position.y]![position.x - 1]!.char === ObstacleChar) {
            direction = Directions.Up;
        } else {
            position.x--;
        }
    }

    return { direction, position }
}

function getPuzzle1() {
    let guard: Guard = JSON.parse(JSON.stringify(InitGuard));

    const map = getMap(data);

    while (isInsideMap(guard.position)) {
        const { x, y } = guard.position;
        map[y]![x]!.visited = true;
        guard = move(map, guard);
    };
    return map.reduce((acc, line) => acc + line.filter(char => char.visited).length, 0);
}

function getPuzzle2() {
    let loopPossibilities = 0;

    for (let posY = 0; posY < size.y; posY++) {
        for (let posX = 0; posX < size.x; posX++) {
            if (
                posX === InitGuard.position.x && posY === InitGuard.position.y || 
                InitMap[posY]![posX]!.char === ObstacleChar
            ) {
                continue;
            }

            const testMap = getMap(data);
            testMap[posY]![posX]!.char = ObstacleChar;

            let guard: Guard = JSON.parse(JSON.stringify(InitGuard));

            const visitedStates = new Set<string>();

            while (isInsideMap(guard.position)) {
                const currentState = `${guard.position.x},${guard.position.y},${guard.direction}`;
                
                if (visitedStates.has(currentState)) {
                    loopPossibilities++;
                    break;
                }

                visitedStates.add(currentState);
                guard = move(testMap, guard);
            };
        }
    }
    return loopPossibilities;
}

console.log(getPuzzle1());
console.log(getPuzzle2());