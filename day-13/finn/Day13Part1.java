import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day13Part1 {

    private static final long A_COIN_COST = 3;
    private static final long B_COIN_COST = 1;

    private static List<ClawMachineConfig> getInput() throws IOException {
        Path path = Paths.get("day-13/finn/resource.txt");
        Scanner scanner = new Scanner(path);

        List<String> allLines = new ArrayList<>();
        while(scanner.hasNextLine()){
            allLines.add(scanner.nextLine());
        }
        scanner.close();

        return allLines.stream()
                .map(line -> {
                    final List<Integer> list = Arrays.stream(line.split(", ")).map(Integer::parseInt).toList();
                    return new ClawMachineConfig(
                            list.get(0),
                            list.get(1),
                            list.get(2),
                            list.get(3),
                            list.get(4),
                            list.get(5)
                    );
                }
                )
                .toList();
    }

    public static void main(String[] args) throws IOException {
        /*final List<ClawMachineConfig> input = List.of(
                new ClawMachineConfig(94, 34, 22, 67, 8400, 5400),
                new ClawMachineConfig(26, 66, 67, 21, 12748, 12176),
                new ClawMachineConfig(17, 86, 84, 37, 7870, 6450),
                new ClawMachineConfig(69, 23, 27, 71, 18641, 10279)
        );*/

        final List<ClawMachineConfig> input = getInput();

        long costs = 0;
        for (ClawMachineConfig config : input) {
            costs += getCostsForPrice(config);
        }
        System.out.println("Result: " + costs);
    }

    private static long getCostsForPrice(ClawMachineConfig config) {
        for (int a = 0; a * config.getAButtonX() <= config.getPriceX() && a * config.getAButtonY() <= config.getPriceY(); a++) {
            for (int b = 0; b * config.getBButtonX() <= config.getPriceX() && b * config.getBButtonY() <= config.getPriceY(); b++) {
                if (a * config.getAButtonX() + b * config.getBButtonX() == config.getPriceX()
                        && a * config.getAButtonY() + b * config.getBButtonY() == config.getPriceY()) {
                    return a * A_COIN_COST + b * B_COIN_COST;
                }
            }
        }
        return 0;
    }

    private static class ClawMachineConfig {

        private final long aButtonX;
        private final long aButtonY;

        private final long bButtonX;
        private final long bButtonY;

        private final long priceX;
        private final long priceY;

        public ClawMachineConfig(
                long aButtonX,
                long aButtonY,
                long bButtonX,
                long bButtonY,
                long priceX,
                long priceY
        ) {
            this.aButtonX = aButtonX;
            this.aButtonY = aButtonY;
            this.bButtonX = bButtonX;
            this.bButtonY = bButtonY;
            this.priceX = priceX;
            this.priceY = priceY;
        }

        public long getAButtonX() {
            return aButtonX;
        }

        public long getAButtonY() {
            return aButtonY;
        }

        public long getBButtonX() {
            return bButtonX;
        }

        public long getBButtonY() {
            return bButtonY;
        }

        public long getPriceX() {
            return priceX;
        }

        public long getPriceY() {
            return priceY;
        }
    }

}
