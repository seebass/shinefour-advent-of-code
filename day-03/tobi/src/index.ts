import * as fs from 'fs';

const memory = fs.readFileSync('./src/input-1.txt').toString()

// part 1

const multiplyRegex = /mul\((\d{1,3}),(\d{1,3})\)/g;
const matches = memory.match(multiplyRegex);

let result1 = 0;

if (matches) {
  result1 = matches.reduce((acc, cum) => {
      const splitted = cum.split(/[(),]/);
      const product = Number(splitted[1]) * Number(splitted[2]);
      return acc + product;
  }, 0)
}

console.log('part 1: uncorrupted memory', result1);

// part 2

const doRegex = /\bdo\(\)/g;
const dontRegex = /\bdon't\(\)/g;

const multiplyMatches = Array.from(memory.matchAll(multiplyRegex));
const doMatches = Array.from(memory.matchAll(doRegex));
const dontMatches = Array.from(memory.matchAll(dontRegex));

function isEnabled(index: number): boolean {
    const doIndex = doMatches.findLast((match) => match.index < index);
    const dontIndex = dontMatches.findLast((match) => match.index < index);
    if (!dontIndex) {
        return true
    }
    if (!doIndex) {
        return false;
    }
    return doIndex.index > dontIndex.index;
}

let result2 = 0;

if (multiplyMatches) {
    result2 = multiplyMatches.reduce((acc, cum) => {
        const enabled = isEnabled(cum.index)
        if (!enabled) {
            return acc;
        }
        const splitted = cum[0].split(/[(),]/);
        const product = Number(splitted[1]) * Number(splitted[2]);
        return acc + product;
    }, 0)
}

console.log('part 2: uncorrupted memory', result2);
