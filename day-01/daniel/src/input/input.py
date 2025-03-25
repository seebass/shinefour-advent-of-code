import os

path_to_file = os.path.dirname(__file__) + "/input.txt"


def read(input_file_path=path_to_file):
    left_list = []
    right_list = []
    with open(input_file_path) as input_file:
        for line in input_file:
            l, r = [int(x) for x in line.split()]
            left_list.append(l)
            right_list.append(r)

    return (left_list, right_list)
