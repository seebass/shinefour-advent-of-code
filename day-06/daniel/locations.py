from __future__ import annotations

from dataclasses import dataclass
from enum import Enum
from typing import NamedTuple


class RelativeLocation(NamedTuple):
    row_movement: int = 0
    column_movement: int = 0


class Directions(Enum):
    UP = RelativeLocation(-1, 0)
    RIGHT = RelativeLocation(0, 1)
    DOWN = RelativeLocation(1, 0)
    LEFT = RelativeLocation(0, -1)


class Location:
    row: int
    column: int
    value: str
    was_visited: bool

    @property
    def is_obstruction(self) -> bool:
        return self.value == "#"

    @property
    def is_guard_position(self) -> bool:
        return self.value in ["^", ">", "v", "<"]

    def __init__(self, row: int, column: int, value: str):
        self.row = row
        self.column = column
        self.value = value
        self.was_visited = self.is_guard_position


# @dataclass
# class TurningPoint:
#     position: Location
#     new_direction: Directions
#     steps_since_last: int

Area = dict[tuple[int, int], Location]
Position = tuple[Location, Directions]
Path = list[Position]
