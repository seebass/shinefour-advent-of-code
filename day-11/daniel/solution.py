from math import log10, floor
from timeit import default_timer as timer
from input import stones
from collections import defaultdict


def has_equal_digit_count(stone: int):
    return (floor(log10(stone)) + 1) % 2 == 0


def split_in_half(stone: int):
    num = stone
    split = [0, 0]

    digits = []
    while num > 0:
        digits.append(num % 10)
        num = floor(num / 10)

    half = len(digits) // 2
    # digits is reversed because we used modulo, so right, left are reversed
    # the for loop will take care of reversion within the lists
    right, left = digits[:half], digits[half:]
    multiplier = 1
    for digit_l, digit_r in zip(left, right):
        split[0] += digit_l * multiplier
        split[1] += digit_r * multiplier
        multiplier = multiplier * 10

    return split


def apply_rules(stone: int):
    if stone == 0:
        return 1
    elif has_equal_digit_count(stone):
        l, r = split_in_half(stone)
        return (l, r)
    else:
        return stone * 2024


def flatten_to_gen(list: list[int | list[int]]):
    for i in list:
        try:
            for sub_i in i:
                yield sub_i
        except:
            yield i


def solve_part_1():
    after_application = (s for s in stones)
    for _ in range(0, 25):
        applied = (apply_rules(s) for s in after_application)
        after_application = flatten_to_gen(applied)
    return sum((1 for _ in after_application))


def solve_part_2():
    stones_count = defaultdict(int)
    for s in stones:
        stones_count[s] += 1

    for _ in range(0, 75):
        for stone, count in dict(stones_count).items():
            if count == 0:
                continue

            result = apply_rules(stone)
            if isinstance(result, tuple):
                l, r = result
                stones_count[l] += count
                stones_count[r] += count
            else:
                stones_count[result] += count

            stones_count[stone] -= count

    return sum([c for _, c in stones_count.items()])


print(f"Solution to part 1: {solve_part_1()}")
print(f"Solution to part 2: {solve_part_2()}")
