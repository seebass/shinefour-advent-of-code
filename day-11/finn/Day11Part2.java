import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day11Part2 {

    private static final int BLINKS = 75;
    private static final int MULTIPLIER = 2024;

    private static List<Long> getInput() {
        return new ArrayList<>(List.of(1950139L, 0L, 3L, 837L, 6116L, 18472L, 228700L, 45L));
    }

    public static void main(String[] args) {
        //List<Long> stones = new ArrayList<>(List.of(125L, 17L));
        List<Long> stones = getInput();

        long stoneCount = 0;
        // Key1: Blinks, Key2: Stone Value
        Map<Integer, Map<Long, Long>> knownStones = new HashMap<>();

        for (long stone : stones) {
            System.out.println("Stone: " + stone);
            stoneCount += handleStone(stone, BLINKS, knownStones);
        }

        System.out.println("Result:" + stoneCount);
    }

    private static long handleStone(Long stone, int remainingBlinks, Map<Integer, Map<Long, Long>> knownStones) {
        if (remainingBlinks == 0) {
            return 1L;
        }

        if (knownStones.get(remainingBlinks) != null && knownStones.get(remainingBlinks).get(stone) != null) {
            return knownStones.get(remainingBlinks).get(stone);
        }

        knownStones.computeIfAbsent(remainingBlinks, k -> new HashMap<>());
        knownStones.get(remainingBlinks).computeIfAbsent(stone, k -> 0L);

        // If the stone is engraved with the number 0, it is replaced by a stone engraved with the number 1.
        if (stone == 0) {
            long stoneCount = handleStone(1L, remainingBlinks - 1, knownStones);
            knownStones.get(remainingBlinks).put(stone, knownStones.get(remainingBlinks).get(stone) + stoneCount);
            return stoneCount;
        }
        // If the stone is engraved with a number that has an even number of digits, it is replaced by two stones.
        // The left half of the digits are engraved on the new left stone, and the right half of the digits are engraved on the new right stone.
        // (The new numbers don't keep extra leading zeroes: 1000 would become stones 10 and 0.)
        if (stone.toString().length() % 2 == 0) {
            String valueStr = stone.toString();
            int mid = valueStr.length() / 2;
            long leftValue = Long.parseLong(valueStr.substring(0, mid));
            long rightValue = Long.parseLong(valueStr.substring(mid));

            long stoneCount1 = handleStone(leftValue, remainingBlinks - 1, knownStones);
            long stoneCount2 = handleStone(rightValue, remainingBlinks - 1, knownStones);
            knownStones.get(remainingBlinks).put(stone, knownStones.get(remainingBlinks).get(stone) + stoneCount1 + stoneCount2);
            return stoneCount1 + stoneCount2;
        }
        // If none of the other rules apply, the stone is replaced by a new stone; the old stone's number multiplied by 2024 is engraved on the new stone.
        long stoneCount = handleStone(stone * MULTIPLIER, remainingBlinks - 1, knownStones);
        knownStones.get(remainingBlinks).put(stone, knownStones.get(remainingBlinks).get(stone) + stoneCount);
        return stoneCount;
    }

}

