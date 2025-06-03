import * as fs from 'fs';

const input = fs.readFileSync('./src/input.txt').toString().split('\n').filter((row) => row.trim() !== '')

// part 1

function isPossibleEquationPart1(inputString: string) {
    const [target, valuesString] = inputString.split(':');
    const values = valuesString.trim().split(' ').map((string) => parseInt(string))
    const possiblePaths = getPossibleResultsPart1(parseInt(target), values[0], values.slice(1));
    return possiblePaths.map((path) => path[path.length - 1]).includes(parseInt(target))
}

function getPossibleResultsPart1(target: number, start: number, values: number[], path: number[] = [], results: number[][] = []) {
    if (!path.length) {
        path.push(start);
    }
    if (!values.length) {
        results.push([...path]);
        return results;
    }

    // multiply
    path.push(path[path.length - 1] * values[0]);
    getPossibleResultsPart1(target, start, values.slice(1), path, results);
    path.pop();

    // sum
    path.push(path[path.length - 1] + values[0]);
    getPossibleResultsPart1(target, start, values.slice(1), path, results);
    path.pop();

    return results;
}

const resultPart1 = input.reduce((acc: number, cum: string) => {
    if (!isPossibleEquationPart1(cum)) {
        return acc;
    }
    return acc + parseInt(cum.split(':')[0])
}, 0);

console.log('solution part 1:', resultPart1)

// part 2

function isPossibleEquationPart2(inputString: string) {
    const [target, valuesString] = inputString.split(':');
    const values = valuesString.trim().split(' ').map((string) => parseInt(string))
    const possiblePaths = getPossibleResultsPart2(parseInt(target), values[0], values.slice(1));
    return possiblePaths.map((path) => path[path.length - 1]).includes(parseInt(target))
}

function getPossibleResultsPart2(target: number, start: number, values: number[], path: number[] = [], results: number[][] = []) {
    if (!path.length) {
        path.push(start);
    }
    if (!values.length) {
        results.push([...path]);
        return results;
    }

    // multiply
    path.push(path[path.length - 1] * values[0]);
    getPossibleResultsPart2(target, start, values.slice(1), path, results);
    path.pop();

    // sum
    path.push(path[path.length - 1] + values[0]);
    getPossibleResultsPart2(target, start, values.slice(1), path, results);
    path.pop();

    // concatenate
    path.push(parseInt(path[path.length - 1].toString() + values[0].toString()));
    getPossibleResultsPart2(target, start, values.slice(1), path, results);
    path.pop();

    return results;
}

const resultPart2 = input.reduce((acc: number, cum: string) => {
    if (!isPossibleEquationPart2(cum)) {
        return acc;
    }
    return acc + parseInt(cum.split(':')[0])
}, 0);

console.log('solution part 2:', resultPart2)
