import os

demo_input = os.path.dirname(__file__) + "/demo.txt"
proper_input = os.path.dirname(__file__) + "/input.txt"


def read(input_file_path=str):
    with open(input_file_path) as input_file:
        return input_file.read().splitlines()


word_search = read(proper_input)

word_search_height = len(word_search)
word_search_width = len(word_search[0])
