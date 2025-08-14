from itertools import groupby, product
from operator import itemgetter
from input import input

Plots = list[list[str]]
Location = tuple[int, int]
TypesByPlot = dict[Location, str]
GardenGraph = dict[Location, list[Location]]
MAX_PERIMETER_LEN = 4
MAX_CORNER_COUNT = 4


def get_types(plots: Plots) -> TypesByPlot:
    return {
        (row, col): char
        for row, chars in enumerate(plots)
        for col, char in enumerate(chars)
    }


def get_next_location(a: Location, b: Location) -> Location:
    row_a, col_a = a
    row_b, col_b = b

    return (row_a + row_b, col_a + col_b)


def get_adjacent_same_type(
    types: TypesByPlot,
) -> GardenGraph:
    directions = [(-1, 0), (0, 1), (1, 0), (0, -1)]
    neighbors = {}
    for loc, type in types.items():
        neighbors[loc] = []
        for dir in directions:
            try:
                next_loc = get_next_location(loc, dir)
                next_type = types[next_loc]
                if type == next_type:
                    neighbors[loc].append(next_loc)
            except:
                continue

    return neighbors


def get_region(start: Location, garden: GardenGraph, region: set[Location] = set()):
    if start in region:
        return

    region.add(start)
    for n in garden[start]:
        get_region(n, garden, region)


def get_regions(garden: GardenGraph):
    regions: list[Location] = []
    for l in garden.keys():
        if any([l in r for r in regions]):
            continue
        region = set()
        get_region(l, garden, region)
        regions.append(region)

    return regions


def calc_fence_price_part_1(regions: list[set[Location]], garden: GardenGraph) -> int:
    total = 0
    for r in regions:
        size = len(r)
        perimeter = sum([MAX_PERIMETER_LEN - len(garden[l]) for l in r])
        total += size * perimeter

    return total


def count_horizontal_sides(region: set[Location]):
    side_count = 0

    sorted_by_row = sorted(region, key=itemgetter(0, 1))
    grouped = [list(g) for _, g in groupby(sorted_by_row, key=itemgetter(0))]

    for group, direction in product(grouped, (-1, 1)):
        continues = False
        for row, col in group:
            has_neighbor_in_direction = (row + direction, col) in region
            if not continues and not has_neighbor_in_direction:
                side_count += 1
                # catches gaps, where sides bow inwards
                continues = (row, col + 1) in region
            elif not has_neighbor_in_direction:
                # catches gaps, where sides bow inwards
                continues = (row, col + 1) in region
            elif has_neighbor_in_direction:
                continues = False

    return side_count


def count_vertical_sides(region: set[Location]):
    side_count = 0

    sorted_by_col = sorted(region, key=itemgetter(1, 0))
    grouped = [list(g) for _, g in groupby(sorted_by_col, key=itemgetter(1))]

    for group, direction in product(grouped, (-1, 1)):
        continues = False
        for row, col in group:
            has_neighbor_in_direction = (row, col + direction) in region
            if not continues and not has_neighbor_in_direction:
                side_count += 1
                # catches gaps, where sides bow inwards
                continues = (row + 1, col) in region
            elif not has_neighbor_in_direction:
                # catches gaps, where sides bow inwards
                continues = (row + 1, col) in region
            elif has_neighbor_in_direction:
                continues = False

    return side_count


def calc_fence_price_part_2(regions: list[set[Location]], garden: GardenGraph) -> int:
    total = 0
    for r in regions:
        size = len(r)
        sides = count_horizontal_sides(r) + count_vertical_sides(r)
        total += size * sides

    return total


def solve_part_1():
    plots = get_types(input)
    garden = get_adjacent_same_type(plots)
    regions = get_regions(garden)
    return calc_fence_price_part_1(regions, garden)


def solve_part_2():
    plots = get_types(input)
    garden = get_adjacent_same_type(plots)
    regions = get_regions(garden)
    return calc_fence_price_part_2(regions, garden)


print(f"Solution to part 1: {solve_part_1()}")
print(f"Solution to part 2: {solve_part_2()}")
