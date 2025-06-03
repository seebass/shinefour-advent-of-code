import * as fs from 'fs';

const inputPageOrderingRules = fs.readFileSync('./src/input_page_ordering_rules.txt').toString().split('\n')
const inputPagesToProduce = fs.readFileSync('./src/input_pages_to_produce.txt').toString().split('\n')
const rulesArray: string[][] = inputPageOrderingRules
    .map((rule) => rule.split('|')).filter((rules) => rules.length === 2);

// part 1

const filteredInputPagesToProduce = inputPagesToProduce.filter((pagesString) => {
    const pagesArray = pagesString.split(',')
    if (pagesArray.length < 2) {
        return false;
    }
    return rulesArray.every((rule) => followsRule(pagesArray, rule));
})

const pagesArrays = filteredInputPagesToProduce
    .map((pages) => pages.split(',').map((page) => parseInt(page)))
const medianValues = pagesArrays.map((pagesArray) => getMedian(pagesArray))
const summedMedianValues = medianValues.reduce((acc, cum) => acc + cum, 0);

console.log('result part 1:', summedMedianValues)

function followsRule(pagesArray: string[], rule: string[]) {
    const [lowerPage, higherPage] = rule
    if (!pagesArray.includes(lowerPage) || !pagesArray.includes(higherPage)) {
        return true
    }
    return pagesArray.indexOf(lowerPage) < pagesArray.indexOf(higherPage)
}

function getMedian(pagesArray: number[]) {
    return pagesArray.at(Math.min(pagesArray.length / 2))!
}

//part 2

const filteredInputPagesWithErrors = inputPagesToProduce.filter((pagesString) => {
    const pagesArray = pagesString.split(',')
    if (pagesArray.length < 2) {
        return false;
    }
    return !rulesArray.every((rule) => followsRule(pagesArray, rule));
})

const pagesArraysWithError = filteredInputPagesWithErrors
    .map((pages) => pages.split(','))
const sortedPagesArraysWithError = pagesArraysWithError
    .map((pagesArrayWithErrors) => sortPages(pagesArrayWithErrors))

const partTwoSortedPages = sortedPagesArraysWithError
    .map((pages) => pages
        .map((page) => parseInt(page)))
const partTwoMedianValues = partTwoSortedPages
    .map((pagesArray) => getMedian(pagesArray))
const summedPartTwoMedianValues = partTwoMedianValues
    .reduce((acc, cum) => acc + cum, 0);

console.log('result part 2:', summedPartTwoMedianValues)

function sortPages(pagesArray: string[], run = 1) {
    if(run>100) {
        console.log('ran 100 times', pagesArray)
        return pagesArray;
    }
    let isModified = false;
    rulesArray.forEach(([lowerPage, higherPage]: string[]) => {
        if (!lowerPage || !higherPage) {
            return;
        }
        if (followsRule(pagesArray, [lowerPage, higherPage])) {
            return;
        }
        const lowerIndex = pagesArray.indexOf(lowerPage)
        const higherIndex = pagesArray.indexOf(higherPage)
        pagesArray[lowerIndex] = higherPage
        pagesArray[higherIndex] = lowerPage
        isModified = true;
        return
    })
    if (isModified) {
        return sortPages(pagesArray, run + 1)
    }
    return pagesArray
}
