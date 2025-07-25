from collections import deque
import os


def read(input_file_path=str):
    with open(input_file_path) as input_file:
        input = input_file.read()
        numbers = input.split()
        return [int(num) for num in numbers]


# stones = read(os.path.dirname(__file__) + "/demo.txt")
stones = read(os.path.dirname(__file__) + "/input.txt")
