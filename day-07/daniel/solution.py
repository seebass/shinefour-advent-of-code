from collections.abc import Callable
from input import equations


def multiply(a: int, b: int):
    return a * b


def add(a: int, b: int):
    return a + b


def combine(a: int, b: int):
    return int(str(a) + str(b))


def try_operations(
    left: int, rights: list[int], additional_ops: list[Callable[[int, int], int]] = []
):
    values: list[int] = []

    rights_clone = rights[:]
    right = rights_clone.pop(0)

    for o in [add, multiply, *additional_ops]:
        value = o(left, right)
        if len(rights_clone) > 0:
            values.extend(try_operations(value, rights_clone, additional_ops))
        else:
            values.append(value)

    return values


def solve_part_1():
    true_values = []
    for e in equations:
        value = e["test_value"]
        left, *rights = e["numbers"]
        all_possible_values = try_operations(left, rights)
        if any([v == value for v in all_possible_values]):
            true_values.append(value)
    return sum(true_values)


def solve_part_2():
    true_values = []
    for e in equations:
        value = e["test_value"]
        left, *rights = e["numbers"]
        all_possible_values = try_operations(left, rights, [combine])
        if any([v == value for v in all_possible_values]):
            true_values.append(value)
    return sum(true_values)


print(f"Solution to part 1: {solve_part_1()}")
print(f"Solution to part 2: {solve_part_2()}")
