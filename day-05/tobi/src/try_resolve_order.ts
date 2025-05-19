import * as fs from 'fs';

// Versuch, anhand der Rules eine Reihenfolge zu bestimmen mit der alle Reihenfolgen abgeglichen werden können
// Stack Limit erreicht nach knapp 5000 Durchläufen, also Fehler in der Logik

const inputPageOrderingRules = fs.readFileSync('./src/input_page_ordering_rules.txt').toString().split('\n')
const inputPagesToProduce = fs.readFileSync('./src/input_pages_to_produce.txt').toString().split('\n')

// part 1

const occurrences = inputPageOrderingRules.reduce((acc, rule) => {
    const [lowerPage, higherPage] = rule.split('|')
    if (!lowerPage || !higherPage) {
        return acc;
    }
    if (!acc[lowerPage]) {
        acc[lowerPage] = 1;
    }
    acc[lowerPage] += 1;
    return acc;
},
{} as Record<string, number>
)

const sortedDescByOccurrence = Object.entries(occurrences)
    .sort(([, aValue], [, bValue]) => bValue - aValue)
    .map(([key]) => key);

inputPageOrderingRules.forEach((rule) => {
    const [lowerPage, higherPage] = rule.split('|')
    if (!lowerPage || !higherPage) {
        console.log(lowerPage, higherPage);
        return;
    }
    if (!sortedDescByOccurrence.includes(higherPage)) {
        sortedDescByOccurrence.push(higherPage);
    }
})
const sortedDescByOccurrenceAsNumbers = sortedDescByOccurrence.map((page) => parseInt(page))

let order: number[] = [];
inputPageOrderingRules.forEach((rule) => {
    const [lowerPage, higherPage] = rule.split('|').map((page) => parseInt(page))
    if (!lowerPage || !higherPage) {
        return;
    }
    const lowerIndex = order.indexOf(lowerPage)
    const higherIndex = order.indexOf(higherPage)
    if (lowerIndex === higherIndex) {
        order.unshift(lowerPage)
        order.push(higherPage)
        return;
    }
    if (lowerIndex === -1) {
        order.unshift(lowerPage)
        return
    }
    if (higherIndex === -1) {
        order.push(higherPage)
        return
    }
    if (lowerIndex < higherIndex) {
        return
    }
    order.splice(higherIndex, 1)
    order.splice(lowerIndex, 0, higherPage)
    return;
})

const result = sortRules(sortedDescByOccurrenceAsNumbers)
console.log('result:', result)

function sortRules(order: number[], run = 1) {
    if(run>5) {
        return order;
    }
    console.log('order', run.toString(), ':', order)
    let isModified = false;
    inputPageOrderingRules.forEach((rule) => {
        const [lowerPage, higherPage] = rule.split('|').map((page) => parseInt(page))
        if (!lowerPage || !higherPage) {
            return;
        }
        const lowerIndex = order.indexOf(lowerPage)
        const higherIndex = order.indexOf(higherPage)
        if (lowerIndex < higherIndex) {
            return
        }
        order.splice(higherIndex, 1)
        order.splice(lowerIndex, 0, higherPage)
        isModified = true
        return;
    })
    if (isModified) {
        return sortRules(order, run + 1)
    }
    return order;
}
