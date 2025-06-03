from itertools import pairwise
from locations import Directions, Location, Area, Path, Position
from input import area, start


def turn_right(direction: Directions):
    match direction:
        case Directions.UP:
            return Directions.RIGHT
        case Directions.RIGHT:
            return Directions.DOWN
        case Directions.DOWN:
            return Directions.LEFT
        case Directions.LEFT:
            return Directions.UP


def get_next_position(current: Position) -> None | Position:
    loc, dir = current
    row_move, col_move = dir.value

    next_pos = (loc.row + row_move, loc.column + col_move)
    if not next_pos in area:
        return None

    next_location = area[next_pos]
    return (next_location, dir)


def walk_to_obstruction(start: Position) -> tuple[Path, Location | None]:
    current_pos = start
    path: Path = []

    while True:
        current_pos[0].was_visited = True
        path.append(current_pos)

        next_pos = get_next_position(current_pos)
        if next_pos == None or next_pos[0].is_obstruction:
            return (path, next_pos)
        else:
            current_pos = next_pos


def walk_area_until_leaving(start: Position) -> Path:
    path: Path = []
    current_pos = start

    has_left = False
    while has_left == False:
        if len(path) > 10000:
            raise RuntimeError("Guard does not leave the area")

        sub_path, obstruction_at = walk_to_obstruction(current_pos)
        path.extend(sub_path)

        if obstruction_at == None:
            has_left = True
        else:
            new_dir = turn_right(current_pos[1])
            # pop so the position before an obstruction isn't added twice
            current_pos = (path.pop()[0], new_dir)

    return path


# def get_turning_points(path: list[tuple[Location, Directions]]):
#     turning_points: list[TurningPoint] = []

#     steps_since_turn = 0
#     for last, current in pairwise(path):
#         steps_since_turn += 1
#         _, last_direction = last
#         current_location, current_direction = current
#         if last_direction != current_direction:
#             t_point = TurningPoint(
#                 current_location, current_direction, steps_since_turn
#             )
#             turning_points.append(t_point)
#             steps_since_turn = 0

#     return turning_points


# def find_loop_possibilities(
#     turning_points: list[TurningPoint], all_locations: dict[tuple[int, int], Location]
# ):
#     loops: list[(Location, Location, Location, Location)] = []
#     if len(turning_points) < 3:
#         return loops
#     for i, p in enumerate(turning_points[2:], 2):
#         second_to_last_turn = turning_points[i - 2]
#         last_turn = turning_points[i - 1]
#         potential_turn_pos = Location(
#             p.position.row
#             + p.new_direction.value.row_movement * last_turn.steps_since_last,
#             p.position.column
#             + p.new_direction.value.column_movement * last_turn.steps_since_last,
#             ".",
#         )
#         missing_obstruction_pos = (
#             potential_turn_pos.row + p.new_direction.value.row_movement,
#             potential_turn_pos.column + p.new_direction.value.column_movement,
#         )
#         # check if obstruction would need to be placed at guard position -> no loop
#         if not missing_obstruction_pos in all_locations:
#             continue
#         if all_locations[missing_obstruction_pos].is_guard_position:
#             continue

#         cannot_complete = False
#         # check if any location between p and missing corner of rectangle is an obstruction -> no loop
#         for step in range(1, last_turn.steps_since_last + 1):
#             pos = (
#                 p.position.row + p.new_direction.value.row_movement * step,
#                 p.position.column + p.new_direction.value.column_movement * step,
#             )
#             # if obstruction is between p and potential_turn -> no loop
#             if not pos in all_locations or all_locations[pos].is_obstruction:
#                 cannot_complete = True
#                 break

#         if cannot_complete:
#             continue

#         # check if any location between missing corner of rectangle and second_to_last_turn is an obstruction -> no loop
#         for step in range(1, p.steps_since_last):
#             pos = (
#                 potential_turn_pos.row + p.new_direction.value.row_movement * -1 * step,
#                 potential_turn_pos.column
#                 + p.new_direction.value.column_movement * -1 * step,
#             )
#             # if obstruction is between potential_turn and second_to_last_turn -> no loop
#             if not pos in all_locations or all_locations[pos].is_obstruction:
#                 cannot_complete = True
#                 break

#         if cannot_complete:
#             continue

#         loops.append(
#             (
#                 p.position,
#                 last_turn.position,
#                 second_to_last_turn.position,
#                 potential_turn_pos,
#             )
#         )

#     return loops


def count_loop_possibilities(path: Path):
    count = 0
    for i, pos in enumerate(path):
        next_pos = get_next_position(pos)
        if next_pos == None or next_pos[0].is_guard_position:
            continue

        loc, dir = pos
        # turn right at location because of newly placed obstruction
        new_dir = turn_right(dir)
        # walk to next obstruction
        potential_loop_part, _ = walk_to_obstruction((loc, new_dir))
        # check if l and d after turning at obstruction match previous entry from path
        matcher_loc, matcher_dir = potential_loop_part[-1]
        matcher_dir = turn_right(matcher_dir)
        if not matcher_loc.is_guard_position and (matcher_loc, matcher_dir) in path[:i]:
            count += 1

    return count


def solve_part_1():
    walk_area_until_leaving(start)
    visited = [l for l in area.values() if l.was_visited]
    return len(visited)


def solve_part_2():
    path = walk_area_until_leaving(start)
    loop_count = count_loop_possibilities(path)
    return loop_count


print(f"Solution to part 1: {solve_part_1()}")
print(f"Solution to part 2: {solve_part_2()}")
