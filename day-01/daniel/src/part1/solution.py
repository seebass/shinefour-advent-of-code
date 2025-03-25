from input import read as read_input


def main():
    lists = read_input()
    left_list, right_list = [sorted(l) for l in lists]

    distances = [abs(l - r) for l, r in zip(left_list, right_list)]

    print(sum(distances))


if __name__ == "__main__":
    main()
