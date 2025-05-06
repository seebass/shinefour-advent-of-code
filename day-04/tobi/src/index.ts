import * as fs from 'fs';

const input = fs.readFileSync('./src/input-1.txt').toString().split('\n').map((line) => line.split(''))

// part 1

function matchesMAS(letters: string[]) {
    return letters[0] === 'M' && letters[1] === 'A' && letters[2] === 'S';
}

function matchesHorizontalForwards(line: number, position: number) {
    if (input[line].length < position + 4) {
        return false
    }
    return matchesMAS(input[line].slice(position + 1))
}

function matchesHorizontalBackwards(line: number, position: number) {
    if (position < 3) {
        return false
    }
    const inputLine = input[line]
    return matchesMAS([inputLine[position - 1], inputLine[position - 2], inputLine[position - 3]])
}

function matchesVerticalForwards(line: number, position: number) {
    if (input.length < line + 4) {
        return false
    }
    return matchesMAS([input[line + 1][position], input[line + 2][position], input[line + 3][position]])
}

function matchesVerticalBackwards(line: number, position: number) {
    if (line < 3) {
        return false
    }
    return matchesMAS([input[line - 1][position], input[line - 2][position], input[line - 3][position]])
}

function matchesDiagonalForwardsUpwards(line: number, position: number) {
    if (position < 3 || input.length < line + 4) {
        return false
    }
    return matchesMAS([input[line + 1][position - 1], input[line + 2][position - 2], input[line + 3][position - 3]])
}

function matchesDiagonalForwardsDownwards(line: number, position: number) {
    if (input[line].length < position + 4 || input.length < line + 4) {
        return false
    }
    return matchesMAS([input[line + 1][position + 1], input[line + 2][position + 2], input[line + 3][position + 3]])
}

function matchesDiagonalBackwardsUpwards(line: number, position: number) {
    if (position < 3 || line < 3) {
        return false
    }
    return matchesMAS([input[line - 1][position - 1], input[line - 2][position - 2], input[line - 3][position - 3]])
}

function matchesDiagonalBackwardsDownwards(line: number, position: number) {
    if (input[line].length < position + 4 || line < 3) {
        return false
    }
    return matchesMAS([input[line - 1][position + 1], input[line - 2][position + 2], input[line - 3][position + 3]])
}

function countXMAS(line: number, position: number) {
    return [
        matchesHorizontalForwards(line, position),
        matchesHorizontalBackwards(line, position),
        matchesVerticalForwards(line, position),
        matchesVerticalBackwards(line, position),
        matchesDiagonalForwardsUpwards(line, position),
        matchesDiagonalForwardsDownwards(line, position),
        matchesDiagonalBackwardsUpwards(line, position),
        matchesDiagonalBackwardsDownwards(line, position),
    ].filter((match) => match).length
}

const result1 = input.reduce((totalOccurrences, lines, lineIndex) => {
    return totalOccurrences + lines.reduce((occurrencesInLine, letterOfLine, letterIndex) => {
        if (letterOfLine !== 'X') {
            return occurrencesInLine;
        }
        return occurrencesInLine + countXMAS(lineIndex, letterIndex)
    }, 0)
}, 0)

console.log('part 1: ', result1);

// part 2

function matchesMAndS(letters: string[]) {
    return letters[0] === 'M' && letters[1] === 'S' || letters[0] === 'S' && letters[1] === 'M';
}

function countMatchesXMAS(line: number, position: number) {
    if (line === 0 || line === input.length || position === 0 || position === input[line].length) {
        return 0;
    }
    return matchesMAndS([input[line - 1][position - 1], input[line + 1][position + 1]]) && matchesMAndS([input[line - 1][position + 1], input[line + 1][position - 1]]) ? 1 : 0
}

const result2 = input.reduce((totalOccurrences, lines, lineIndex) =>
    totalOccurrences + lines.reduce((occurrencesInLine, letterOfLine, letterIndex) => {
        if (letterOfLine !== 'A') {
            return occurrencesInLine;
        }
        return occurrencesInLine + countMatchesXMAS(lineIndex, letterIndex)
    }, 0), 0)

console.log('part 2: ', result2);
