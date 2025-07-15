import { getPuzzle } from "../utils";

type Disk = Array<number | null>;
type DiskSegment = {numberSegments: number, fileSegment: number | null};

const puzzle = await getPuzzle(2024, 9);

function mapDiskChunks(puzzle: string) {
    let currentFileId = 0;
    return puzzle.split('').map((numberSegments, index) => {
        const fileSegment = index % 2 === 0 ? currentFileId : null;
        if (fileSegment !== null) {
            currentFileId++;
        }
        return {
            numberSegments: Number(numberSegments),
            fileSegment
        };
    });
}

function mapDiskSegments(diskChunks: Array<DiskSegment>) {
    return diskChunks.reduce((acc, diskSegment) => 
        [...acc, ...Array(Number(diskSegment.numberSegments)).fill(diskSegment.fileSegment)], <Disk>[]);
}

function compactDiskSegmants(disk: Disk) {
    let firstFreeIndex = 0;
    let lastFileBlockIndex = disk.length - 1;

    const compactedDisk = [...disk];

    while (true) {
        while (firstFreeIndex < compactedDisk.length && compactedDisk[firstFreeIndex] !== null) {
            firstFreeIndex++;
        }

        while (lastFileBlockIndex >= 0 && compactedDisk[lastFileBlockIndex] === null) {
            lastFileBlockIndex--;
        }

        if (firstFreeIndex >= lastFileBlockIndex) {
            break;
        }

        compactedDisk[firstFreeIndex] = compactedDisk[lastFileBlockIndex]!;
        compactedDisk[lastFileBlockIndex] = null;
    }

    return compactedDisk;
}

function compactDiskChunks(diskSegments: Array<DiskSegment>) {
    let compactedDiskSegments = [...diskSegments];

    for (let index = compactedDiskSegments.length - 1; index >= 0; index--) {
        if (compactedDiskSegments[index]!.fileSegment === null) {
            continue;
        }
        const freeSpaceForFileIndex = compactedDiskSegments.findIndex((diskSegment, freeIndex) => 
            freeIndex < index &&
            diskSegment.fileSegment === null && 
            diskSegment.numberSegments >= compactedDiskSegments[index]!.numberSegments
        );
        if (freeSpaceForFileIndex >= 0) {
            const freeSpaceAfterMove = compactedDiskSegments[freeSpaceForFileIndex]!.numberSegments - compactedDiskSegments[index]!.numberSegments;
            let compactedDiskSegmentsAfterMove = [...compactedDiskSegments.slice(0, freeSpaceForFileIndex), compactedDiskSegments[index]!];
            if (freeSpaceAfterMove) {
                compactedDiskSegmentsAfterMove.push({fileSegment: null, numberSegments: freeSpaceAfterMove});
            }
            compactedDiskSegmentsAfterMove = [
                ...compactedDiskSegmentsAfterMove,
                ...compactedDiskSegments.slice(freeSpaceForFileIndex + 1, index),
                { fileSegment: null, numberSegments: compactedDiskSegments[index]!.numberSegments }
            ];
            if (index != compactedDiskSegments.length - 1) {
                compactedDiskSegmentsAfterMove.push(...compactedDiskSegments.slice(index + 1, compactedDiskSegments.length))
            }
            if (compactedDiskSegmentsAfterMove.length > compactedDiskSegments.length) {
                index++;
            }
            compactedDiskSegments = compactedDiskSegmentsAfterMove;
        }
    }

    return compactedDiskSegments;
}

function checkSum(compactedDisk: Disk) {
    return compactedDisk.reduce((acc: number, segment, index) => segment ? acc + (index * segment) : acc, 0);
}

const diskChunks: Array<DiskSegment> = mapDiskChunks(puzzle);

console.log("Puzzle 1: " + checkSum(compactDiskSegmants(mapDiskSegments(diskChunks))));
console.log("Puzzle 2: " + checkSum(mapDiskSegments(compactDiskChunks(diskChunks))));
