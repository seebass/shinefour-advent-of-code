import { array1, array2 } from "./input-data"

// part 1: total difference

const sortedArray1 = array1.sort((a, b) => a - b)
const sortedArray2 = array2.sort((a, b) => a - b)

const differences = sortedArray1.map((item1, index) => Math.abs(item1 - sortedArray2[index]))

const sumOfDifferences = differences.reduce((sum1, sum2) => sum1 + sum2, 0);

console.log("sumOfDifferences", sumOfDifferences)

// part 2: similarity score

const scores = array1.map((item1) => array2.filter((item2) => item1 === item2).length *= item1)
const similarityScore = scores.reduce((sum1, sum2) => sum1 + sum2, 0);
console.log("similarityScore", similarityScore)
