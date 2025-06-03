import os
from locations import Area, Directions, Location, Position


def read(input_file_path=str):
    with open(input_file_path) as input_file:
        return input_file.read().splitlines()


def get_area(raw_area: list[str]) -> Area:
    area_height = len(raw_area)
    area_width = len(raw_area[0])
    return {
        (row, column): Location(row, column, raw_area[row][column])
        for column in range(area_width)
        for row in range(area_height)
    }


def get_start(area: Area) -> Position:
    positions = [l for l in area.values() if l.is_guard_position]
    if len(positions) != 1:
        raise ValueError("More than one guard starting positions found.")
    position = positions[0]

    direction = Directions.UP
    match position.value:
        case ">":
            direction = Directions.RIGHT
        case "v":
            direction = Directions.DOWN
        case "<":
            direction = Directions.LEFT

    return (position, direction)


# area = get_area(read(os.path.dirname(__file__) + "/demo.txt"))
area = get_area(read(os.path.dirname(__file__) + "/input.txt"))
start = get_start(area)
