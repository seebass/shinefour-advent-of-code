from locations import Location, Directions, SearchInstruction
from utils import (
    is_inside_word_search,
    get_letter_at,
    get_every_location,
    search_word_from,
)


def find_xmas():
    all_locations = get_every_location()
    x_locations = [l for l in all_locations if get_letter_at(l) == "X"]
    nested_results = [
        search_word_from(start=l, direction=d, search_term="XMAS")
        for l in x_locations
        for d in Directions
    ]
    return [r for r in nested_results if r != None]


locations = find_xmas()
print(f"Solution to part 1: {len(locations)}")


def get_cross_searches(a_location: Location) -> list[SearchInstruction]:
    searches = [
        (Directions.UP_LEFT, Directions.DOWN_RIGHT),
        (Directions.UP_RIGHT, Directions.DOWN_LEFT),
        (Directions.DOWN_LEFT, Directions.UP_RIGHT),
        (Directions.DOWN_RIGHT, Directions.UP_LEFT),
    ]
    row, column = a_location.row, a_location.column
    start_locations = [
        SearchInstruction(Location(row, column).move(start_from), search_to)
        for start_from, search_to in searches
    ]
    return [l for l in start_locations if is_inside_word_search(l[0])]


def is_crossing(searches: list[SearchInstruction]):
    results = [
        search_word_from(start=s, direction=d, search_term="MAS") for s, d in searches
    ]
    empty_removed = [r for r in results if r != None and len(r) > 0]
    # check for length 2 because in a cross configuration only exactly 2 can ever exist
    return len(empty_removed) == 2


def find_crossed_mas():
    all_locations = get_every_location()
    a_locations = [l for l in all_locations if get_letter_at(l) == "A"]
    searches = [get_cross_searches(l) for l in a_locations]
    return len([s for s in searches if is_crossing(s)])


print(f"Solution to part 2: {find_crossed_mas()}")
