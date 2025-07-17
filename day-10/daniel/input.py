import os


def read(input_file_path=str):
    with open(input_file_path) as input_file:
        input = input_file.read()
        lines = input.splitlines()
        return [[int(char) for char in line] for line in lines]


# map = read(os.path.dirname(__file__) + "/demo.txt")
map = read(os.path.dirname(__file__) + "/input.txt")
