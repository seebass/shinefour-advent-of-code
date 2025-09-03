import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day14Part1 {

    private static final int SECONDS = 100;
    private static final int AREA_SIZE_X = 101; //11
    private static final int AREA_SIZE_Y = 103; //7

    private static long robotCountInArea00 = 0;
    private static long robotCountInArea01 = 0;
    private static long robotCountInArea10 = 0;
    private static long robotCountInArea11 = 0;

    private static List<RobotConfig> getInput() throws IOException {
        Path path = Paths.get("day-14/finn/resource.txt");
        Scanner scanner = new Scanner(path);

        List<String> allLines = new ArrayList<>();
        while (scanner.hasNextLine()) {
            allLines.add(scanner.nextLine());
        }
        scanner.close();

        return allLines.stream()
                .map(line -> {
                            final List<Integer> list = Arrays.stream(line.split(", ")).map(Integer::parseInt).toList();
                            return new RobotConfig(
                                    list.get(0),
                                    list.get(1),
                                    list.get(2),
                                    list.get(3)
                            );
                        }
                )
                .toList();
    }

    public static void main(String[] args) throws IOException {
        /*final List<RobotConfig> input = List.of(
                new RobotConfig(0, 4, 3, -3),
                new RobotConfig(6, 3, -1, -3),
                new RobotConfig(10, 3, -1, 2),
                new RobotConfig(2, 0, 2, -1),
                new RobotConfig(0, 0, 1, 3),
                new RobotConfig(3, 0, -2, -2),
                new RobotConfig(7, 6, -1, -3),
                new RobotConfig(3, 0, -1, -2),
                new RobotConfig(9, 3, 2, 3),
                new RobotConfig(7, 3, -1, 2),
                new RobotConfig(2, 4, 2, -3),
                new RobotConfig(9, 5, -3, -3)
        );*/

        final List<RobotConfig> input = getInput();

        for (RobotConfig config : input) {
            config.setFinalPosition(
                    ((config.getStartX() + config.getVelocityX() * SECONDS) % AREA_SIZE_X + AREA_SIZE_X) % AREA_SIZE_X,
                    ((config.getStartY() + config.getVelocityY() * SECONDS) % AREA_SIZE_Y + AREA_SIZE_Y) % AREA_SIZE_Y
            );
            addToArea(config);
        }
        System.out.println("Area00: " + robotCountInArea00);
        System.out.println("Area01: " + robotCountInArea01);
        System.out.println("Area10: " + robotCountInArea10);
        System.out.println("Area11: " + robotCountInArea11);
        System.out.println("Result: " + robotCountInArea00 * robotCountInArea01 * robotCountInArea10 * robotCountInArea11);
    }

    private static void addToArea(final RobotConfig config) {
        if (config.getFinalX() < AREA_SIZE_X / 2) {
            if (config.getFinalY() < AREA_SIZE_Y / 2) {
                // area 00
                robotCountInArea00++;
            } else if (config.getFinalY() > AREA_SIZE_Y / 2) {
                // area 01
                robotCountInArea01++;
            }
        } else if (config.getFinalX() > AREA_SIZE_X / 2) {
            if (config.getFinalY() < AREA_SIZE_Y / 2) {
                // area 10
                robotCountInArea10++;
            } else if (config.getFinalY() > AREA_SIZE_Y / 2) {
                // area 11
                robotCountInArea11++;
            }
        }
    }

    private static class RobotConfig {

        private final int startX;
        private final int startY;

        private final int velocityX;
        private final int velocityY;

        private int finalX;

        private int finalY;

        public RobotConfig(
                int startX,
                int startY,
                int velocityX,
                int velocityY
        ) {
            this.startX = startX;
            this.startY = startY;
            this.velocityX = velocityX;
            this.velocityY = velocityY;
        }

        public int getStartX() {
            return startX;
        }

        public int getStartY() {
            return startY;
        }

        public int getVelocityX() {
            return velocityX;
        }

        public int getVelocityY() {
            return velocityY;
        }

        public void setFinalPosition(int finalX, int finalY) {
            this.finalX = finalX;
            this.finalY = finalY;
        }

        public int getFinalX() {
            return finalX;
        }

        public int getFinalY() {
            return finalY;
        }

    }

}
