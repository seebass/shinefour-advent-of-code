import os


def read(input_file_path=str):
    with open(input_file_path) as input_file:
        input = input_file.read()
        lines = input.splitlines()
        return [char for char in lines]


# input = read(os.path.dirname(__file__) + "/demo.txt")
input = read(os.path.dirname(__file__) + "/input.txt")
