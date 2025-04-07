import * as fs from 'fs';

const reports = fs.readFileSync('./src/input-1.txt')
    .toString()
    .split('\n')
    .filter((report) => report.length)
    .map((report) => report.split(' ').map((level) => Number(level)))

// part 1

function isIncreasing(report: number[]): boolean {
    let slicedReport = report.slice(0, report.length - 1);
    return slicedReport.map((level, index) => report[index + 1] - level >= 1 && report[index + 1] - level <= 3).every((value) => value)
}

function isDecreasing(report: number[]): boolean {
    let slicedReport = report.slice(0, report.length - 1);
    return slicedReport.map((level, index) => report[index + 1] - level <= -1 && report[index + 1] - level >= -3).every((value) => value)
}

function checkIsReportSafe(report: number[]): boolean {
    if (report.length === 1) return true;
    return isIncreasing(report) || isDecreasing(report);
}

const numberOfSafeReportsPart1 = reports.reduce((sum, report) => checkIsReportSafe(report) ? sum + 1 : sum, 0);
console.log('part 1: number of safe reports', numberOfSafeReportsPart1);

// part 2

function getReportVariants(report: number[]): number[][] {
    return report.map((_, index) => report.toSpliced(index, 1))
}

const numberOfSafeReportsPart2 = reports
    .map((report) => getReportVariants(report))
    .reduce((sum, reportVariants) => reportVariants.some((reportVariant) => checkIsReportSafe(reportVariant)) ? sum + 1 : sum, 0);
console.log('part 2: number of safe reports', numberOfSafeReportsPart2);
