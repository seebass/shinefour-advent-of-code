import os


def read(input_file_path=str):
    with open(input_file_path) as input_file:
        return input_file.read().splitlines()


# input = read(os.path.dirname(__file__) + "/demo.txt")
input = read(os.path.dirname(__file__) + "/input.txt")

equations = []
for line in input:
    val, nums = line.split(":")
    equations.append(
        {"test_value": int(val), "numbers": [int(n) for n in nums.split()]}
    )
