import os
from rules import Rule


def read(input_file_path=str):
    with open(input_file_path) as input_file:
        raw_rules, raw_page_nrs = input_file.read().split("\n\n")

        page_nrs = [list(map(int, p.split(","))) for p in raw_page_nrs.splitlines()]

        split_rules_str = [r.split("|") for r in raw_rules.splitlines()]
        rules = [Rule(int(before), int(after)) for before, after in split_rules_str]

        return (rules, page_nrs)


# rules, page_nrs = read(os.path.dirname(__file__) + "/demo.txt")
rules, page_nrs = read(os.path.dirname(__file__) + "/input.txt")
