import { getPuzzle, splitStringByLines } from "../utils";

const data = await getPuzzle(2024, 5);

const splittedLines = splitStringByLines(data);

const rules: Array<string> = [];
const pageNumbersList: Array<string> = [];
splittedLines.forEach(line => {
    if (!line) {
        return;
    }
    if (/^\d+\|\d+$/.test(line)) {
        rules.push(line);
    }else {
        pageNumbersList.push(line);
    }
});

let middleCorrectPages = 0;
let middleSortedPages = 0;

function sortPages(pagesToOrder: Array<string>) {
    const sortedList: string[] = [];
    let remainingPages = [...pagesToOrder];

    while (remainingPages.length > 0) {
        const readyToProcess: string[] = [];

        for (const candidatePage of remainingPages) {
            const hasUnmetPrerequisite = rules.some(rule => {
                const [prerequisite, dependent] = rule.split('|');

                return dependent === candidatePage && prerequisite && pagesToOrder.includes(prerequisite) && !sortedList.includes(prerequisite);
            });

            if (!hasUnmetPrerequisite) {
                readyToProcess.push(candidatePage);
            }
        }

        if (readyToProcess.length === 0) {
            break;
        }

        readyToProcess.sort((a, b) => Number(a) - Number(b));
        const nextPage = readyToProcess[0];

        if (!nextPage) {
            break;
        }

        sortedList.push(nextPage);
        remainingPages = remainingPages.filter(page => page !== nextPage);
    }

    return sortedList;
}

for (let pageNumbersListIndex = 0; pageNumbersListIndex < pageNumbersList.length; pageNumbersListIndex++) {
    const pageNumbers = pageNumbersList[pageNumbersListIndex];
    if (!pageNumbers) {
        continue;
    }
    let numbers = pageNumbers.split(',').map(s => s.trim());
    let correctOrder = true;
    for (let i = 0; i < numbers.length - 1 && correctOrder; i++) {
        const num1 = numbers[i];
        const num2 = numbers[i + 1];
        if (!num1 || !num2) {
            continue;
        }
        if (!rules.includes(`${num1}|${num2}`)) {
            correctOrder = false;
            numbers = sortPages(numbers);
        }
    }
    const middleIndex = Math.floor(numbers.length / 2);
    if (correctOrder) {
        middleCorrectPages += Number(numbers[middleIndex]);
    } else {
        middleSortedPages += Number(numbers[middleIndex]);
    }
}

console.log(middleCorrectPages);
console.log(middleSortedPages);
