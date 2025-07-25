public class Day11Part1 {

    private static final int BLINKS = 25;
    private static final int MULTIPLIER = 2024;

    private static LinkedNode<Long> getInput() {
        LinkedNode<Long> input = new LinkedNode<>(null, null, 1950139L);
        input.addAfter(45L);
        input.addAfter(228700L);
        input.addAfter(18472L);
        input.addAfter(6116L);
        input.addAfter(837L);
        input.addAfter(3L);
        input.addAfter(0L);

        return input;
    }

    public static void main(String[] args) {
        // LinkedNode<Long> stone = new LinkedNode<>(null, null, 125);
        // stone.addAfter(17);
        LinkedNode<Long> stone = getInput();

        for (int blink = 1; blink <= BLINKS; blink++) {
            LinkedNode<Long> current = stone.getFirst();
            while (current != null) {
                handleStone(current);
                current = current.getNext();
            }
        }

        System.out.println("Result:" + stone.getSize());
    }

    private static void handleStone(LinkedNode<Long> stone) {
        // If the stone is engraved with the number 0, it is replaced by a stone engraved with the number 1.
       if (stone.getValue() == 0) {
           stone.setValue(1L);
           return;
       }
       // If the stone is engraved with a number that has an even number of digits, it is replaced by two stones.
        // The left half of the digits are engraved on the new left stone, and the right half of the digits are engraved on the new right stone.
        // (The new numbers don't keep extra leading zeroes: 1000 would become stones 10 and 0.)
       if (stone.getValue().toString().length() % 2 == 0) {
           String valueStr = stone.getValue().toString();
           int mid = valueStr.length() / 2;
           long leftValue = Long.parseLong(valueStr.substring(0, mid));
           long rightValue = Long.parseLong(valueStr.substring(mid));

           stone.addBefore(leftValue);
           stone.setValue(rightValue);
           return;
       }
       // If none of the other rules apply, the stone is replaced by a new stone; the old stone's number multiplied by 2024 is engraved on the new stone.
       stone.setValue(stone.getValue() * MULTIPLIER);
    }
}
