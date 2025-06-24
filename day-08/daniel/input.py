import os


def read(input_file_path=str):
    with open(input_file_path) as input_file:
        return input_file.read().splitlines()


Map = list[list[str]]
# map = read(os.path.dirname(__file__) + "/demo.txt")
map = read(os.path.dirname(__file__) + "/input.txt")
