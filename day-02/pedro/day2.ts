import { getPuzzle, splitStringByLines } from "./utils";

const data = await getPuzzle(2024, 2);

const splittedLines = splitStringByLines(data);

function isStepUnSafe(value1: number, value2: number, direction: boolean) {
    if (value1 === value2) {
        return true;
    }

    if ((direction && (value1 > value2)) || (!direction && (value1 < value2))) {
        return true;
    }

    const distanceBetweenValues = Math.abs(value2 - value1);
    if (distanceBetweenValues > 3) {
        return true;
    }
    return false;
}

function getSafeReports(useDampener = false) {
    return splittedLines.filter((line, i) => {
        const values = line.split(" ");

        if (line.length < 2) {
            return false;
        }

        let direction = true;
        let skippedByDampener = false;
    
        const isUnsafe = values.some((value, index) => {
            if (skippedByDampener || index === values.length - 1) {
                skippedByDampener = false;
                return false;
            }
    
            const value1 = Number(value);
            const value2 = Number(values[index + 1]);

            if (index===0) {
                direction = value1 < value2;
            }

            const unsafeStep = isStepUnSafe(value1, value2, direction);

            if (!unsafeStep || !useDampener || index + 2 >= values.length) {
                return unsafeStep;
            }

            const value3 = Number(values[index + 2]);
            const lastValue = Number(values[index - 1]);

            const unsafeWithDampenerValue2 = isStepUnSafe(value1, value3, direction);
            const unsafeWithDampenerValue1 = isStepUnSafe(lastValue, value2, direction);

            if (!unsafeWithDampenerValue2) {
                skippedByDampener = true;
            }

            if (index===1 && unsafeWithDampenerValue1 && unsafeWithDampenerValue2) {
                const unsafeStep = isStepUnSafe(value1, value2, !direction);
                if (!unsafeStep) {
                    direction = !direction;
                    return false;
                }
            }

            return unsafeWithDampenerValue1 && unsafeWithDampenerValue2;
        });
    
        return !isUnsafe;
    });
}

const safeReportsWithoutDampener = getSafeReports();
const safeReportsWithDampener = getSafeReports(true);

console.log("Teil 1:", safeReportsWithoutDampener.length)
console.log("Teil 2:", safeReportsWithDampener.length)