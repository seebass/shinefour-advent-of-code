import { getPuzzle, splitStringByLines } from "../utils";

const data = await getPuzzle(2024, 7);
const splittedLines = splitStringByLines(data);

const Operations = {
    Add: 'ADD',
    Multiply: 'MULTIPLY',
    Concatenation: 'CONCATENATION'
};

function testOperations(operations: Array<typeof Operations[keyof typeof Operations]>) {
    return splittedLines.reduce((total, line) => {
        const splittedLine = line.split(': ');
        const testValue = Number(splittedLine[0]);
        const [firstNumber, ...numbers] = splittedLine[1]!.split(' ');
        
        const equationResults = numbers!.reduce((allResults, numStr) => {
            const num = Number(numStr);

            return allResults.flatMap(res => {
                const newMap = [];
                if (operations.includes(Operations.Add)) {
                    newMap.push(res + num);
                }
                if (operations.includes(Operations.Multiply)) {
                    newMap.push(res * num);
                }
                if (operations.includes(Operations.Concatenation)) {
                    newMap.push(Number(`${res}${numStr}`));
                }
                return newMap;
            });
        }, [Number(firstNumber)]);

        const isPossible = equationResults.some(result => result === testValue);

        return total + (isPossible ? testValue : 0);
    }, 0);
}

console.log('Puzzle1: ', testOperations([Operations.Add, Operations.Multiply]));
console.log('Puzzle2: ', testOperations([Operations.Add, Operations.Multiply, Operations.Concatenation]));