from input import map

Location = tuple[int, int]
Point = tuple[Location, int]

TRAILHEAD = 0
SUMMIT = 9


def get_values_by_location(map) -> dict[Location, int]:
    return {
        (row, col): num
        for row, values in enumerate(map)
        for col, num in enumerate(values)
    }


def find_next_points(
    starts: list[Point], all_points: dict[Location, int]
) -> dict[Point, list[Point]]:
    """
    Collects all adjacent points (up, down, left, right) for a list of starting points.
    Only points with a value exactly one higher than the current point are collected.
    Collects adjacent points recursively for all adjacent points.
    """
    directions = [(-1, 0), (0, 1), (1, 0), (0, -1)]

    paths = {p: [] for p in starts}
    points_to_check = [*starts]
    while len(points_to_check) > 0:
        point = points_to_check.pop()
        location, val = point
        row, col = location
        for d_row, d_col in directions:
            try:
                next_location = (row + d_row, col + d_col)
                next_value = val + 1
                if all_points[next_location] == next_value:
                    next_point = (next_location, next_value)
                    paths[point].append(next_point)
                    if not next_point in paths:
                        paths[next_point] = []
                        points_to_check.insert(0, next_point)
            except:
                continue
    return paths


def get_summits(start: Point, next_points: dict[Point, list[Point]]):
    """
    Collects summits (value of 9) of all paths from a trailhead.
    Returns duplicate endpoints if multiple paths lead there.
    """
    summits = []
    for p in next_points[start]:
        summits.extend(get_summits(p, next_points))
        if p[1] == SUMMIT:
            summits.append(p)
    return summits


def solve_part_1():
    values_by_location = get_values_by_location(map)
    trailheads = [
        (location, num)
        for location, num in values_by_location.items()
        if num == TRAILHEAD
    ]
    next_points = find_next_points(trailheads, values_by_location)
    unique_summits = [set(get_summits(t, next_points)) for t in trailheads]
    scores = [len(s) for s in unique_summits]
    return sum(scores)


def solve_part_2():
    values_by_location = get_values_by_location(map)
    trailheads = [
        (location, num)
        for location, num in values_by_location.items()
        if num == TRAILHEAD
    ]
    next_points = find_next_points(trailheads, values_by_location)
    ratings = [len(get_summits(t, next_points)) for t in trailheads]
    return sum(ratings)


print(f"Solution to part 1: {solve_part_1()}")
print(f"Solution to part 2: {solve_part_2()}")
