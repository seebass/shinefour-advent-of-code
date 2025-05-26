import { clone } from "npm:ramda";

export function readText(demo = false) {
  const decoder = new TextDecoder("utf-8");
  const text = Deno.readFileSync(`./${demo ? "demo" : "challenge"}.txt`);

  return decoder.decode(text);
}

const text = readText(false);

type Location = {
  x: number;
  y: number;
};

class Direction {
  static readonly NORTH = "^";
  static readonly SOUTH = "v";
  static readonly EAST = ">";
  static readonly WEST = "<";

  static isDirection(char: string) {
    return [
      this.NORTH,
      this.SOUTH,
      this.EAST,
      this.WEST,
    ].includes(char);
  }
}

class OperationMap {
  private static pristineMap = text.split("\n").map((line) => line.split(""));
  private readonly map: string[][];

  constructor() {
    this.map = clone(OperationMap.pristineMap);
  }

  getByLocation({ x, y }: Location): string {
    return this.map[y][x];
  }

  isOnMap({ x, y }: Location) {
    return y >= 0 && y < this.map.length && x >= 0 && x < this.map[0].length;
  }

  findLocationByPredicate(
    predicate: (char: string) => boolean,
  ): Location | undefined {
    for (let y = 0; y < this.map.length; y++) {
      for (let x = 0; x < this.map[y].length; x++) {
        if (predicate(this.map[y][x])) {
          return { x, y };
        }
      }
    }
    return undefined;
  }

  findLocationsByPredicate(predicate: (char: string) => boolean): Location[] {
    const result: Location[] = [];
    for (let y = 0; y < this.map.length; y++) {
      for (let x = 0; x < this.map[y].length; x++) {
        if (predicate(this.map[y][x])) {
          result.push({ x, y });
        }
      }
    }
    return result;
  }

  count(char: string): number {
    return this.map.reduce(
      (sum, row) =>
        sum + row.reduce((sum, cell) => cell === char ? sum + 1 : sum, 0),
      0,
    );
  }

  setByLocation(char: string, { x, y }: Location): void {
    this.map[y][x] = char;
  }
}

class Guard {
  location: Location;

  constructor(private readonly map: OperationMap) {
    this.location = map.findLocationByPredicate((char) =>
      Direction.isDirection(char)
    )!;
  }

  move() {
    switch (this.map.getByLocation(this.location)) {
      case Direction.NORTH:
        this.moveNorth();
        break;
      case Direction.EAST:
        this.moveEast();
        break;
      case Direction.SOUTH:
        this.moveSouth();
        break;
      case Direction.WEST:
        this.moveWest();
        break;
      default:
        throw new Error(
          "Unknown faced direction " + this.map.getByLocation(this.location),
        );
    }
  }

  private moveNorth() {
    this.moveTo({ x: this.location.x, y: this.location.y - 1 });
  }

  private moveEast() {
    this.moveTo({ x: this.location.x + 1, y: this.location.y });
  }

  private moveSouth() {
    this.moveTo({ x: this.location.x, y: this.location.y + 1 });
  }

  private moveWest() {
    this.moveTo({ x: this.location.x - 1, y: this.location.y });
  }

  private moveTo(
    target: Location,
  ) {
    if (this.map.isOnMap(target)) {
      if (this.map.getByLocation(target) === "#") {
        this.turnRight();
      } else {
        const direction = this.map.getByLocation(this.location);
        this.map.setByLocation("X", this.location);
        this.map.setByLocation(direction, target);
        this.location = target;
      }
    } else {
      this.map.setByLocation("X", this.location);
      this.location = target;
    }
  }

  private turnRight() {
    const facedDirection = this.map.getByLocation(this.location);
    let newFacedDirection: string;
    switch (facedDirection) {
      case Direction.NORTH:
        newFacedDirection = Direction.EAST;
        break;
      case Direction.EAST:
        newFacedDirection = Direction.SOUTH;
        break;
      case Direction.SOUTH:
        newFacedDirection = Direction.WEST;
        break;
      case Direction.WEST:
        newFacedDirection = Direction.NORTH;
        break;
      default:
        throw new Error("Unknown faced direction");
    }
    this.map.setByLocation(newFacedDirection, this.location);
  }
}

function nextObstacle(map: OperationMap, options: Location[]) {
  if (!options.length) {
    return undefined;
  }

  const next = options[0];
  options.splice(0, 1);
  map.setByLocation("#", next);
  return next;
}

function findPossibleObstacleLocations(): Location[] {
  const map = new OperationMap();
  const guard = new Guard(map);
  const startingLocation = guard.location;
  let location: Location = startingLocation;
  while (map.isOnMap(location)) {
    guard.move();
    location = guard.location;
  }
  return map.findLocationsByPredicate((char) => char === "X").filter((
    location,
  ) =>
    !(location.x === startingLocation.x && location.y === startingLocation.y)
  );
}

function partOne() {
  const map = new OperationMap();
  const guard = new Guard(map);
  let location: Location = guard.location;
  while (map.isOnMap(location)) {
    guard.move();
    location = guard.location;
  }
  return map.count("X");
}

function partTwo() {
  const possibleObstaclePositions = findPossibleObstacleLocations();
  const obstaclePositions = new Set<string | undefined>();

  let map = new OperationMap();
  let guard = new Guard(map);
  let visitedPositions = new Set<string>();
  let obstacleLocation = nextObstacle(map, possibleObstaclePositions);
  let location: Location | undefined = guard.location;

  visitedPositions.add(
    `${location?.x}/${location?.y}/${map.getByLocation(location)}`,
  );

  function resetForNextObstacle() {
    map = new OperationMap();
    guard = new Guard(map);
    visitedPositions = new Set<string>();
    obstacleLocation = nextObstacle(map, possibleObstaclePositions);
    location = guard.location;
  }

  while (map.isOnMap(guard.location) && obstacleLocation) {
    guard.move();
    location = guard.location;
    if (map.isOnMap(location)) {
      const oldSize = visitedPositions.size;
      visitedPositions.add(
        `${location.x}/${location.y}/${map.getByLocation(location)}`,
      );
      if (oldSize === visitedPositions.size) {
        obstaclePositions.add(`${obstacleLocation.x}/${obstacleLocation.y}`);
        resetForNextObstacle();
      }
    } else {
      resetForNextObstacle();
    }
  }
  return obstaclePositions.size;
}

console.log("Part One", partOne());
console.log("Part Two", partTwo());
