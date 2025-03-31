import os
from typing import List

path_to_file = os.path.dirname(__file__) + "/input.txt"


def read(input_file_path=path_to_file):
    reports: List[List[int]] = []
    with open(input_file_path) as input_file:
        for line in input_file:
            levels = [int(x) for x in line.split()]
            reports.append(levels)

    return reports
