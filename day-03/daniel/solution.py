import re
from input import read as read_input

memory = read_input()


def extract_operands(memory: str) -> list[(int, int)]:
    mul_instruction_re = re.compile(r"mul\((\d{1,3}),(\d{1,3})\)")
    matches = re.findall(mul_instruction_re, memory)
    return [
        (int(multiplicand), int(multiplier)) for multiplicand, multiplier in matches
    ]


def calc_products(operands: list[(int, int)]) -> int:
    return [multiplicand * multiplier for multiplicand, multiplier in operands]


operands = extract_operands(memory)
products = calc_products(operands)
print(f"Solution to part 1: {sum(products)}")


def extract_enabled_memory(memory: str) -> list[str]:
    # get enabled start of memory, collect rest -> every chunk now starts disabled
    enabled_beginning, *rest = memory.split("don't()")
    # discard disabled start of chunk and chunks containing no enabled memory
    enabled_rest = [r.split("do()", 1)[1] for r in rest if "do()" in r]

    return [enabled_beginning, *enabled_rest]


enabled = "".join(extract_enabled_memory(memory))
operands_part_2 = extract_operands(enabled)
products_part_2 = calc_products(operands_part_2)
print(f"Solution to part 2: {sum(products_part_2)}")
