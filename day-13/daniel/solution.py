from functools import reduce
from math import floor
from input import configs


Config = tuple[tuple[int, int], tuple[int, int], tuple[int, int]]


def calc_max_presses(config: Config):
    btn_a, btn_b, prize = config
    max_a = min(100, floor((prize[0] / btn_a[0])), floor((prize[1] / btn_a[1])))
    max_b = min(100, floor((prize[0] / btn_b[0])), floor((prize[1] / btn_b[1])))

    return (max_a, max_b)


def calc_btn_presses_1(config: Config) -> None | tuple[int, int]:
    a, b, prize = config
    max_a, max_b = calc_max_presses(config)
    can_reach = (
        a[0] * max_a + b[0] * max_b >= prize[0]
        and a[1] * max_a + b[1] * max_b >= prize[1]
    )
    if not can_reach:
        return None

    hits_prize = (
        lambda a_presses, b_presses: a[0] * a_presses + b[0] * b_presses == prize[0]
        and a[1] * a_presses + b[1] * b_presses == prize[1]
    )
    for a_presses in range(max_a, -1, -1):

        for b_presses in range(max_b + 1):
            if hits_prize(a_presses, b_presses):
                return (a_presses, b_presses)

    return None


TOKEN_COST_A = 3
TOKEN_COST_B = 1

# ---------- solve part 1 ----------
presses = [calc_btn_presses_1(c) for c in configs]
presses = [p for p in presses if p != None]
cost = reduce(
    lambda acc, p: acc + p[0] * TOKEN_COST_A + p[1] * TOKEN_COST_B, presses, 0
)
print(f"solution part 1: {cost}")


def find_a_presses(ax: int, ay: int, bx: int, by: int, px: int, py: int) -> float:
    axby = ax * by
    pxby = px * by
    aybx = ay * bx
    pybx = py * bx

    return (pxby - pybx) / (axby - aybx)


def find_b_presses(ay: int, by: int, py: int, a_presses: float):
    return (py - ay * a_presses) / by


def calc_btn_presses_2(config: Config) -> tuple[int, int] | None:
    a, b, prize = config
    ax, ay = a
    bx, by = b
    px, py = prize
    a_presses = find_a_presses(ax, ay, bx, by, px, py)
    b_presses = find_b_presses(ay, by, py, a_presses)

    if a_presses.is_integer() and b_presses.is_integer():
        return (int(a_presses), int(b_presses))
    else:
        return None


delta = 10000000000000
configs = [(a, b, (prize[0] + delta, prize[1] + delta)) for a, b, prize in configs]

# ---------- solve part 2 ----------
presses = [calc_btn_presses_2(c) for c in configs]
presses = [p for p in presses if p != None]
cost = reduce(
    lambda acc, p: acc + p[0] * TOKEN_COST_A + p[1] * TOKEN_COST_B, presses, 0
)
# 79224826701628 is too low
print(f"solution part 2: {cost}")
