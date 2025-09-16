from itertools import batched
import os
import re


def read(input_file_path: str) -> list[tuple[int, int, int, int]]:
    num_re = re.compile("-?\\d+")
    with open(input_file_path) as input_file:
        input = input_file.read()
        nums = map(int, num_re.findall(input))
        return list(batched(nums, 4))


# configs = read(os.path.dirname(__file__) + "/demo.txt")
configs = read(os.path.dirname(__file__) + "/input.txt")
