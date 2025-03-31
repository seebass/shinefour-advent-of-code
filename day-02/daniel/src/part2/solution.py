from typing import List, Union
from input import read as read_input
from part1.solution import is_gradual, is_one_way


def is_safe(report: List[int]):
    return is_one_way(report) and is_gradual(report)


def has_only_1_bad_level(report: List[int]):
    for i in range(0, len(report)):
        report_copy = report.copy()
        del report_copy[i]
        if is_safe(report_copy):
            return True

    return False


def main():
    reports = read_input()
    safe_reports = []
    potentially_unsafe_reports = []

    for r in reports:
        safe_reports.append(r) if is_safe(r) else potentially_unsafe_reports.append(r)

    safe_reports.extend(
        [r for r in potentially_unsafe_reports if has_only_1_bad_level(r)]
    )

    print(len(safe_reports))


if __name__ == "__main__":
    main()
