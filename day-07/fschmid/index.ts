import { sum } from "npm:ramda";

export function readText(demo = false) {
  const decoder = new TextDecoder("utf-8");
  const text = Deno.readFileSync(`./${demo ? "demo" : "challenge"}.txt`);

  return decoder.decode(text);
}

const text = readText(false);

enum RESULT {
  TOO_LOW,
  TOO_HIGH,
  EQUAL,
}

class Equation {
  constructor(readonly testValue: number, readonly operands: number[]) {
  }

  calculate(operators: OperatorFn[]) {
    const result = operators.reduce((result, operator, index) => {
      if (index === 0) {
        return operator(this.operands[index], this.operands[index + 1]);
      }
      return operator(result, index + 1);
    }, 0);
    const n = result - this.testValue;
    if (n === 0) {
      return RESULT.EQUAL;
    }
    return n < 0 ? RESULT.TOO_LOW : RESULT.TOO_HIGH;
  }
}

function readEquations() {
  return text
    .split("\n")
    .map((line) => {
      const [testValue, operands] = line
        .split(": ");
      return new Equation(
        Number.parseInt(testValue),
        operands.split(" ").map((o) => Number.parseInt(o, 10)),
      );
    });
}

function testEquation(equation: Equation) {
  const { operands } = equation;
  const operatorCount = operands.length - 1;
  let multiplyCount = 0;
  let result = RESULT.TOO_LOW;
  while (multiplyCount < operands.length && result === RESULT.TOO_LOW) {
    const operatorFns = new Array<OperatorFn>(operatorCount).fill(add);
    for (let i = 0; i < operatorCount; i++) {
      result = addMultiply(operatorFns, equation, multiplyCount);
    }
    multiplyCount++;
  }
  return result ? equation.testValue : 0;
}

type OperatorFn = (a: number, b: number) => number;

function add(a: number, b: number) {
  return a + b;
}

function multiply(a: number, b: number) {
  return a * b;
}

function addMultiply(
  operatorFns: OperatorFn[],
  equation: Equation,
  operatorsToAdd: number = operatorFns.length,
): RESULT {
  if (operatorsToAdd === 0) {
    return equation.calculate(operatorFns);
  }
  const results = [
    addMultiply([...operatorFns], equation, operatorsToAdd - 1),
    ...operatorFns.map((_, i, all) => {
      const newArray = [...all];
      newArray[i] = multiply;
      return addMultiply(newArray, equation, operatorsToAdd - 1);
    }),
  ];
  if (results.some((r) => r === RESULT.EQUAL)) {
    return RESULT.EQUAL;
  }
  if (results.some((r) => r === RESULT.TOO_LOW)) {
    return RESULT.TOO_LOW;
  }
  return RESULT.TOO_HIGH;
}

function partOne() {
  const equations = readEquations();
  sum(equations.map(testEquation));
  return null;
}

function partTwo() {
  return null;
}

console.log("Part One", partOne());
console.log("Part Two", partTwo());
