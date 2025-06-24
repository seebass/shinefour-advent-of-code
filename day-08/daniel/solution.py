from itertools import combinations
from input import Map, map

Coordinates = tuple[int, int]
Distance = tuple[int, int]


def is_on_map(map: Map, coordinates: Coordinates):
    x, y = coordinates
    if x < 0 or y < 0:
        return False
    try:
        return bool(map[x][y])
    except:
        return False


def get_antennas(map: Map):
    coordinates_by_char = {}

    for x in range(len(map)):
        for y in range(len(map[x])):
            char = map[x][y]
            if char == ".":
                continue
            if not char in coordinates_by_char:
                coordinates_by_char[char] = []
            coordinates_by_char[char].append((x, y))

    return coordinates_by_char


def get_antinodes_at_set_distance(antennas: dict[str, list[Coordinates]]):
    all_antinodes: set[Coordinates] = set()

    for coordinates in antennas.values():
        for a, b in combinations(coordinates, 2):
            a_x, a_y = a
            b_x, b_y = b
            distance_x, distance_y = (a_x - b_x, a_y - b_y)
            all_antinodes.add((a_x + distance_x, a_y + distance_y))
            all_antinodes.add((b_x + distance_x * -1, b_y + distance_y * -1))

    return [a for a in all_antinodes if is_on_map(map, a)]


def get_antinodes_ignoring_distance(antennas: dict[str, list[Coordinates]]):
    all_antinodes: set[Coordinates] = set()

    for coordinates in antennas.values():
        for a, b in combinations(coordinates, 2):
            a_x, a_y = a
            b_x, b_y = b
            distance_x, distance_y = (a_x - b_x, a_y - b_y)

            for coord, dir in [
                (a, (distance_x, distance_y)),
                (b, (distance_x * -1, distance_y * -1)),
            ]:
                current = coord
                while is_on_map(map, current):
                    all_antinodes.add(current)
                    current = (current[0] + dir[0], current[1] + dir[1])

    return all_antinodes


def solve_part_1():
    antennas = get_antennas(map)
    antinodes = get_antinodes_at_set_distance(antennas)
    return len(antinodes)


def solve_part_2():
    antennas = get_antennas(map)
    antinodes = get_antinodes_ignoring_distance(antennas)
    return len(antinodes)


print(f"Solution to part 1: {solve_part_1()}")
print(f"Solution to part 2: {solve_part_2()}")
