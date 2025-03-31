from itertools import pairwise
from typing import List
from input import read as read_input


def is_one_way(report: List[int]):
    if len(report) <= 1:
        return True
    if report[0] == report[1]:
        return False
    is_incr = report[0] < report[1]

    def is_strict(x, y):
        return x < y if is_incr else x > y

    return all([is_strict(x, y) for x, y in pairwise(report)])


def is_gradual(report: List[int]):
    if len(report) <= 1:
        return True

    return all([1 <= abs(x - y) <= 3 for x, y in pairwise(report)])


def main():
    reports = read_input()
    safe_reports = [r for r in reports if is_one_way(r) and is_gradual(r)]
    print(len(safe_reports))


if __name__ == "__main__":
    main()
