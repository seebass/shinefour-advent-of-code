from itertools import batched, groupby
from operator import itemgetter
from input import disk_map

type I_FREE_SPACE = str
FREE_SPACE = "."
IMAGE = list[I_FREE_SPACE | int]


def create_img(map: str) -> IMAGE:
    img = []
    current_id = 0
    for batch in batched(map, 2):
        file = batch[0]

        img.extend([current_id] * int(file))
        if len(batch) == 2:
            free = batch[1]
            img.extend([FREE_SPACE] * int(free))

        current_id += 1
    return img


def pack_disk_fragmented(img: IMAGE) -> list[int]:
    packed = []
    free_space_removed = [block for block in img if block != FREE_SPACE]
    target_len = len(free_space_removed)
    for block in img:
        if block == FREE_SPACE:
            packed.append(free_space_removed.pop())
        else:
            packed.append(block)
        if len(packed) == target_len:
            break

    return packed


def pack_disk_contiguously(img: IMAGE) -> list[int]:
    # sort available free spaces by length, giving start and end
    # collect start and end of files
    free_spaces: dict[int, list[tuple[int, int]]] = {}
    files: list[tuple[int, int]] = []
    for char, iterator in groupby(enumerate(img), key=itemgetter(1)):
        start = next(iterator)[0]
        end = start + sum([1 for _ in iterator]) + 1
        if char == FREE_SPACE:
            file_len = end - start
            if not file_len in free_spaces:
                free_spaces[file_len] = []
            free_spaces[file_len].append((start, end))
        else:
            files.append((start, end))
    longest_free_space = max(free_spaces.keys())

    packed = img[:]
    files.reverse()
    for start, end in files:
        # check for free_spaces longer or equally as long as file
        file_len = end - start
        potential_spaces = [
            free_spaces[l][0]
            for l in range(file_len, longest_free_space + 1)
            if l in free_spaces and len(free_spaces[l]) > 0
        ]
        if len(potential_spaces) == 0:
            continue
        potential_spaces.sort(key=itemgetter(0), reverse=True)
        free_start, free_end = potential_spaces.pop()
        if free_start > start:
            continue
        # update packed
        packed[free_start : free_start + file_len] = packed[start:end]
        packed[start:end] = [FREE_SPACE] * file_len

        # update free_spaces
        free_len = free_end - free_start
        free_spaces[free_len].pop(0)
        leftover_len = free_len - file_len
        if leftover_len == 0:
            continue
        if not leftover_len in free_spaces:
            free_spaces[leftover_len] = []
        free_spaces[leftover_len].append((free_start + file_len, free_end))
        free_spaces[leftover_len].sort(key=itemgetter(0))

    return packed


def calc_checksum(img: IMAGE) -> int:
    block_sums = [i * int(id) for i, id in enumerate(img) if id != FREE_SPACE]
    return sum(block_sums)


def solve_part_1():
    img = create_img(disk_map)
    packed = pack_disk_fragmented(img)
    return calc_checksum(packed)


def solve_part_2():
    img = create_img(disk_map)
    packed = pack_disk_contiguously(img)
    return calc_checksum(packed)


print(f"Solution to part 1: {solve_part_1()}")
print(f"Solution to part 2: {solve_part_2()}")
