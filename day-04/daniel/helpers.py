from locations import Location
from input import word_search_height, word_search_width, word_search


def is_inside_word_search(l: Location) -> bool:
    row_is_in = 0 <= l.row < word_search_height
    col_is_in = 0 <= l.column < word_search_width
    return row_is_in and col_is_in


def get_letter_at(l: Location) -> str | None:
    return word_search[l.row][l.column] if is_inside_word_search(l) else None


def get_every_location() -> list[Location]:
    return [
        Location(row, column)
        for column in range(word_search_width)
        for row in range(word_search_height)
    ]
