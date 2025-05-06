import { getPuzzle, splitStringByLines } from "../utils";

const data = await getPuzzle(2024, 4);

const splittedLines = splitStringByLines(data);

function findWordOnGrid(searchedWord: string) {
    let count = 0;
    const reversedSearchedWord = searchedWord.split("").reverse().join("");

    splittedLines.forEach((line, index) => {
        for (let lineIndex = 0; lineIndex < line.length; lineIndex++) {
    
            const lastCharPositionH = lineIndex + searchedWord.length;
            const compareHorizontal = line.length >= lastCharPositionH;
            if (compareHorizontal) {
                const textToCompare = line.slice(lineIndex, lastCharPositionH);
                if (textToCompare === searchedWord || textToCompare === reversedSearchedWord) count++;
            }
    
            const lastCharPositionV = index + searchedWord.length;
            const compareVertical = splittedLines.length >= lastCharPositionV;
            if (compareVertical) {
                let wordCharIndex = 0;
                const relevantLines = splittedLines.slice(index, lastCharPositionV);
                const { verticalText, diagonalTextToRight, diagonalTextToLeft } = relevantLines
                    .reduce((acc, relevantLine) => {
                        const verticalChar = lineIndex < relevantLine.length ? relevantLine.at(lineIndex) : "";
                        const diagonalToRightCharIndex = lineIndex + wordCharIndex;
                        const diagonalToRightChar = diagonalToRightCharIndex < relevantLine.length ? relevantLine.at(diagonalToRightCharIndex) : "";
                        const diagonalToLeftCharIndex = lineIndex - wordCharIndex;
                        const diagonalToLeftChar = diagonalToLeftCharIndex >= 0 ? relevantLine.at(diagonalToLeftCharIndex) : "";
                        wordCharIndex++;
                        return { 
                            verticalText: acc.verticalText + verticalChar, 
                            diagonalTextToRight: acc.diagonalTextToRight + diagonalToRightChar,
                            diagonalTextToLeft: acc.diagonalTextToLeft + diagonalToLeftChar,
                        };
                    }, { verticalText: "", diagonalTextToRight: "", diagonalTextToLeft: "" });
                if (verticalText === searchedWord || verticalText === reversedSearchedWord) count++;
                if (diagonalTextToRight === searchedWord || diagonalTextToRight === reversedSearchedWord) count++;
                if (diagonalTextToLeft === searchedWord || diagonalTextToLeft === reversedSearchedWord) count++;
            }
        }
    });

    return count;
}

function findWordFormingXOnGrid(searchedWord: string) {
    if ((searchedWord.length - 1) % 2 !== 0) {
        console.error("Can't form X on Grid (Word length must be odd)");
        return 0;
    }

    let count = 0;
    const reversedSearchedWord = searchedWord.split("").reverse().join("");
    const charsForX = Math.floor(searchedWord.length / 2);
    const middleChar = searchedWord[charsForX];

    splittedLines.forEach((line, index) => {
        if (index < charsForX || index + charsForX >= splittedLines.length) {
            return;
        }

        for (let lineIndex = charsForX; lineIndex < line.length - charsForX; lineIndex++) {
            if (line.at(lineIndex) !== middleChar) {
                continue;
            }

            const relevantLines = splittedLines.slice(index - charsForX, index + charsForX + 1);
            if (relevantLines.length !== (2 * charsForX + 1)) {
                continue; 
            }

            let wordCharIndex = 0;
            const { diagonalTextToRight, diagonalTextToLeft } = relevantLines
                .reduce((acc, relevantLine) => {
                    const diagonalToRightCharIndex = lineIndex + wordCharIndex - charsForX;
                    const diagonalToRightChar = diagonalToRightCharIndex < relevantLine.length ? relevantLine.at(diagonalToRightCharIndex) : "";
                    const diagonalToLeftCharIndex = lineIndex - wordCharIndex + charsForX;
                    const diagonalToLeftChar = diagonalToLeftCharIndex >= 0 ? relevantLine.at(diagonalToLeftCharIndex) : "";
                    wordCharIndex++;
                    return { 
                        diagonalTextToRight: acc.diagonalTextToRight + diagonalToRightChar,
                        diagonalTextToLeft: acc.diagonalTextToLeft + diagonalToLeftChar,
                    };
                }, { diagonalTextToRight: "", diagonalTextToLeft: "" });

            const isDiagonalRightValid = (diagonalTextToRight === searchedWord || diagonalTextToRight === reversedSearchedWord);
            const isDiagonalLeftValid = (diagonalTextToLeft === searchedWord || diagonalTextToLeft === reversedSearchedWord);
            if (isDiagonalRightValid && isDiagonalLeftValid) count++;
        }
    });

    return count;
}

console.log("Part1: ", findWordOnGrid("XMAS"));
console.log("Part2: ", findWordFormingXOnGrid("MAS"));
