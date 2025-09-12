from enum import IntEnum
from math import floor, prod
from input import configs

# (pos x, pos y, velo x, velo y)
Config = tuple[int, int, int, int]
SECONDS = 100
SpaceSize = IntEnum("SpaceSize", [("X", 101), ("Y", 103)])


def calc_pos_after_secs(config: Config, seconds: int) -> tuple[int, int]:
    px, py, vx, vy = config
    x = (vx * seconds + px) % SpaceSize.X
    y = (vy * seconds + py) % SpaceSize.Y
    return (x, y)


def count_robots_per_quadrant(
    positions: list[tuple[int, int]],
) -> tuple[int, int, int, int]:
    # quadrants are assigned clockwise
    q1 = q2 = q3 = q4 = 0
    # floor because we are counting from 0
    mid_x = floor(SpaceSize.X / 2)
    mid_y = floor(SpaceSize.Y / 2)
    for p in positions:
        match p:
            case (x, y) if x < mid_x and y < mid_y:
                q1 += 1
            case (x, y) if x > mid_x and y < mid_y:
                q2 += 1
            case (x, y) if x > mid_x and y > mid_y:
                q3 += 1
            case (x, y) if x < mid_x and y > mid_y:
                q4 += 1

    return (q1, q2, q3, q4)


# ---------- solve part 1 ----------
pos = [calc_pos_after_secs(c, SECONDS) for c in configs]
safety_factor = prod(count_robots_per_quadrant(pos))
print(f"solution part 1: {safety_factor}")


def generate_map(positions: list[tuple[int, int]]):
    map = ""
    for y in range(SpaceSize.Y):
        line = ""
        for x in range(SpaceSize.X):
            line += "A" if (x, y) in positions else "."
        line += "\n"
        map += line
    return map


# ---------- solve part 2 ----------

seconds = 2167
while True:
    pos = [calc_pos_after_secs(c, seconds) for c in configs]
    map = generate_map(pos)
    print(f"{seconds} seconds")
    print(map)
    # I noticed a repeating vertical pattern appearing in a regular interval - that interval is 101 steps
    seconds += 101
