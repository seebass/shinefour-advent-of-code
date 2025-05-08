from locations import Location, Directions
from input import word_search_height, word_search_width, word_search


def is_inside_word_search(l: Location) -> bool:
    row_is_in = 0 <= l.row < word_search_height
    col_is_in = 0 <= l.column < word_search_width
    return row_is_in and col_is_in


def get_letter_at(l: Location) -> str | None:
    return word_search[l.row][l.column] if is_inside_word_search(l) else None


def search_word_from(
    start: Location, search_term: str, direction: Directions
) -> list[Location]:
    letter_locations: list[Location] = []
    current = Location(start.row, start.column)
    for searched_letter in search_term:
        found_letter = get_letter_at(current)
        if not found_letter == searched_letter:
            break
        letter_locations.append(Location(current.row, current.column))
        current.move(direction)

    return letter_locations if len(letter_locations) == len(search_term) else None


def get_every_location() -> list[Location]:
    return [
        Location(row, column)
        for column in range(word_search_width)
        for row in range(word_search_height)
    ]
