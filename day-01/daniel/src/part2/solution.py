from collections import Counter
from input import read as read_input


def main():
    left_list, right_list = read_input()

    right_counter = Counter(right_list)
    similarity_scores = [l * right_counter[l] for l in left_list]

    print(sum(similarity_scores))


if __name__ == "__main__":
    main()
