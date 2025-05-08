from contextlib import suppress
from functools import cmp_to_key
from itertools import chain
from math import floor
from random import shuffle
from input import rules, page_nrs
from rules import Rule


def conforms_to(rules: list[Rule], page_nrs: list[int]):
    for before, after in rules:
        if not before in page_nrs or not after in page_nrs:
            continue
        if page_nrs.index(before) > page_nrs.index(after):
            return False

    return True


def get_middle_page_nr(page_nrs: list[int]):
    page_count = len(page_nrs)
    if page_count % 2 == 0:
        raise ValueError("Length of page_nrs must be uneven.")
    middle = floor(page_count / 2)
    return page_nrs[middle]


def sort_according_to(rules: list[Rule], page_nrs: list[int]):
    pages = page_nrs.copy()
    relevant_rules = [r for r in rules if r[0] in pages and r[1] in pages]

    def sort_by_relevant():
        for before, after in relevant_rules:
            before_idx = pages.index(before)
            after_idx = pages.index(after)
            if before_idx > after_idx:
                pages.insert(after_idx, pages.pop(before_idx))

    iterations = 0
    while not conforms_to(rules, pages):
        sort_by_relevant()
        iterations += 1
        if iterations > 10000:
            raise RuntimeError("Cannot sort page_nrs - are there conflicting rules?")

    return pages


def solve_part_1():
    pages_in_order = [p for p in page_nrs if conforms_to(rules, p)]
    middle_pages = [get_middle_page_nr(p) for p in pages_in_order]
    return sum(middle_pages)


def solve_part_2():
    unordered_pages_now_ordered = [
        sort_according_to(rules, p) for p in page_nrs if not conforms_to(rules, p)
    ]
    middle_pages_of_prev_unordered = [
        get_middle_page_nr(p) for p in unordered_pages_now_ordered
    ]
    return sum(middle_pages_of_prev_unordered)


print(f"Solution to part 1: {solve_part_1()}")
print(f"Solution to part 2: {solve_part_2()}")
