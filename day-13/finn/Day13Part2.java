import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day13Part2 {

    private static final BigInteger A_COIN_COST = BigInteger.valueOf(3);
    private static final BigInteger B_COIN_COST = BigInteger.valueOf(1);
    private static final BigInteger PRICE_POSITION_INCREMENT = BigInteger.valueOf(10000000000000L);

    private static List<ClawMachineConfig> getInput() throws IOException {
        Path path = Paths.get("day-13/finn/resource.txt");
        Scanner scanner = new Scanner(path);

        List<String> allLines = new ArrayList<>();
        while (scanner.hasNextLine()) {
            allLines.add(scanner.nextLine());
        }
        scanner.close();

        return allLines.stream()
                .map(line -> {
                            final List<Integer> list = Arrays.stream(line.split(", ")).map(Integer::parseInt).toList();
                            return new ClawMachineConfig(
                                    BigInteger.valueOf(list.get(0)),
                                    BigInteger.valueOf(list.get(1)),
                                    BigInteger.valueOf(list.get(2)),
                                    BigInteger.valueOf(list.get(3)),
                                    BigInteger.valueOf(list.get(4)),
                                    BigInteger.valueOf(list.get(5))
                            );
                        }
                )
                .toList();
    }

    public static void main(String[] args) throws IOException {
        /*final List<ClawMachineConfig> input = List.of(
                new ClawMachineConfig(BigInteger.valueOf(94), BigInteger.valueOf(34), BigInteger.valueOf(22), BigInteger.valueOf(67), BigInteger.valueOf(8400), BigInteger.valueOf(5400)),
                new ClawMachineConfig(BigInteger.valueOf(26), BigInteger.valueOf(66), BigInteger.valueOf(67), BigInteger.valueOf(21), BigInteger.valueOf(12748), BigInteger.valueOf(12176)),
                new ClawMachineConfig(BigInteger.valueOf(17), BigInteger.valueOf(86), BigInteger.valueOf(84), BigInteger.valueOf(37), BigInteger.valueOf(7870), BigInteger.valueOf(6450)),
                new ClawMachineConfig(BigInteger.valueOf(69), BigInteger.valueOf(23), BigInteger.valueOf(27), BigInteger.valueOf(71), BigInteger.valueOf(18641), BigInteger.valueOf(10279))
        );*/

        final List<ClawMachineConfig> input = getInput();

        BigInteger costs = BigInteger.ZERO;
        for (ClawMachineConfig config : input) {
            config.incrementPriceY(PRICE_POSITION_INCREMENT);
            config.incrementPriceX(PRICE_POSITION_INCREMENT);

            BigInteger currentCost = getCostsForPrice(config);
            costs = costs.add(currentCost);
        }
        System.out.println("Result: " + costs);
    }

    private static BigInteger getCostsForPrice(ClawMachineConfig config) {
        BigInteger b = config.getPriceY().multiply(config.getAButtonX()).subtract(config.getPriceX().multiply(config.getAButtonY()))
                .divide(config.getBButtonY().multiply(config.getAButtonX()).subtract(config.getBButtonX().multiply(config.getAButtonY())));

        BigInteger a = config.getPriceX().multiply(config.getBButtonY()).subtract(config.getPriceY().multiply(config.getBButtonX()))
                .divide(config.getBButtonY().multiply(config.getAButtonX()).subtract(config.getBButtonX().multiply(config.getAButtonY())));

        if (a.multiply(config.getAButtonX()).add(b.multiply(config.getBButtonX())).equals(config.getPriceX())
                && a.multiply(config.getAButtonY()).add(b.multiply(config.getBButtonY())).equals(config.getPriceY())) {
            return b.multiply(B_COIN_COST).add(a.multiply(A_COIN_COST));
        }

        return BigInteger.ZERO;
    }

    private static class ClawMachineConfig {

        private final BigInteger aButtonX;
        private final BigInteger aButtonY;

        private final BigInteger bButtonX;
        private final BigInteger bButtonY;

        private BigInteger priceX;
        private BigInteger priceY;

        public ClawMachineConfig(
                BigInteger aButtonX,
                BigInteger aButtonY,
                BigInteger bButtonX,
                BigInteger bButtonY,
                BigInteger priceX,
                BigInteger priceY
        ) {
            this.aButtonX = aButtonX;
            this.aButtonY = aButtonY;
            this.bButtonX = bButtonX;
            this.bButtonY = bButtonY;
            this.priceX = priceX;
            this.priceY = priceY;
        }

        public BigInteger getAButtonX() {
            return aButtonX;
        }

        public BigInteger getAButtonY() {
            return aButtonY;
        }

        public BigInteger getBButtonX() {
            return bButtonX;
        }

        public BigInteger getBButtonY() {
            return bButtonY;
        }

        public BigInteger getPriceX() {
            return priceX;
        }

        public BigInteger getPriceY() {
            return priceY;
        }

        public void incrementPriceX(BigInteger increment) {
            this.priceX = this.priceX.add(increment);
        }

        public void incrementPriceY(BigInteger increment) {
            this.priceY = this.priceY.add(increment);
        }

    }

}
