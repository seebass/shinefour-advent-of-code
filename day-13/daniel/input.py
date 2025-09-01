from itertools import batched
import os
import re


def read(input_file_path: str):
    num_re = re.compile("\\d+")
    with open(input_file_path) as input_file:
        input = input_file.read().split("\n\n")
        configs = []
        for config in input:
            nums = num_re.findall(config)
            configs.append(tuple((int(x), int(y)) for x, y in batched(nums, 2)))
    return configs


# configs = read(os.path.dirname(__file__) + "/demo.txt")
configs = read(os.path.dirname(__file__) + "/input.txt")
