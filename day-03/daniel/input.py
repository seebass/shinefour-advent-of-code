import os
from typing import List

path_to_file = os.path.dirname(__file__) + "/input.txt"


def read():
    memory_dump = ""
    with open(path_to_file) as input_file:
        for line in input_file:
            memory_dump += line

    return memory_dump
