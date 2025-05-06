import { getPuzzle } from "../utils";

const data = await getPuzzle(2024, 3);

function getMultipliers(value: string): Array<string> {
    const multipliers = value.match(/mul\(\d+,\d+\)/gm);
    return multipliers ?? [];
}

function multiply(value: Array<string>) {
    return value.reduce((acc, mul) => {
        const numbers = mul.match(/\d+/g);
        if (!numbers) {
            return acc;
        }
        const number1 = Number(numbers[0]);
        const number2 = Number(numbers[1]);
        return acc + (number1 * number2);
    }, 0);
}

function getActiveMultipliers() {
    const actions = data.split(/(?=do\(\)|don't\(\))/gm);
    return actions.reduce((acc, action, index) => {
        if (index !== 0 && action.startsWith("don't()")) {
            return acc;
        }
        return [...acc, ...getMultipliers(action)];
    }, new Array<string>());
}


console.log("Teil 1:", multiply(getMultipliers(data)))
console.log("Teil 2:", multiply(getActiveMultipliers()))