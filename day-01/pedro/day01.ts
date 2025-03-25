import file1 from "./input.txt" with { type: "text" };

const newLineChar = "\n";

const splittedLines = file1.split(newLineChar);

function getSplittedColumns(lines: Array<string>): [Array<number>, Array<number>] {
    const col1: Array<number> = [];
    const col2: Array<number> = [];
    
    lines.forEach((line) => {
        const [value1, value2] = line.split("   ");
        if (!value1 || !value2) return
        col1.push(Number(value1));
        col2.push(Number(value2));
    });

    return [col1, col2];
}

const [col1, col2] = getSplittedColumns(splittedLines);

function solvePart1(col1: Array<number>, col2: Array<number>) {
    col1.sort();
    col2.sort();
    
    return col2.reduce((acc, col2Value, index) => {
        const col1Value = col1[index];
        if (!col1Value) {
            return acc;
        }
        return acc + Math.abs((col2Value - col1Value));
    } ,0);
}

function solvePart2(col1: Array<number>, col2: Array<number>) {
    return col1.reduce((acc, col1Value) => 
        acc + (col1Value * col2.filter(col2Value => col2Value === col1Value).length) ,0);
}


console.log("Part 1: ", solvePart1(col1, col2));


console.log("Part 2: ", solvePart2(col1, col2));