from __future__ import annotations

from dataclasses import dataclass
from enum import Enum
from typing import NamedTuple


class RelativeLocation(NamedTuple):
    row_movement: int = 0
    column_movement: int = 0


class Directions(Enum):
    UP = RelativeLocation(-1, 0)
    UP_RIGHT = RelativeLocation(-1, 1)
    RIGHT = RelativeLocation(0, 1)
    DOWN_RIGHT = RelativeLocation(1, 1)
    DOWN = RelativeLocation(1, 0)
    DOWN_LEFT = RelativeLocation(1, -1)
    LEFT = RelativeLocation(0, -1)
    UP_LEFT = RelativeLocation(-1, -1)


@dataclass
class Location:
    row: int
    column: int

    def move(self, direction: Directions):
        row, col = direction.value
        self.row += row
        self.column += col
        return self
