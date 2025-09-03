import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day14Part2 {

    private static final int SECONDS = 10000;
    private static final int AREA_SIZE_X = 101; //11
    private static final int AREA_SIZE_Y = 103;

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
        final List<RobotConfig> input = getInput();


        for (int second = 1; second <= SECONDS; second++) {
            for (RobotConfig config : input) {
                config.setCurrentPosition(
                        ((config.getStartX() + config.getVelocityX() * second) % AREA_SIZE_X + AREA_SIZE_X) % AREA_SIZE_X,
                        ((config.getStartY() + config.getVelocityY() * second) % AREA_SIZE_Y + AREA_SIZE_Y) % AREA_SIZE_Y
                );
            }
            System.out.println("After second: " + second);
            if (checkForTenInRow(input)) {
                printMap(second, input);
            }
        }
    }

    private static boolean checkForTenInRow(final List<RobotConfig> robots) {
        for (RobotConfig robot : robots) {
            if (checkForTenRobots(0, robots, robot.getCurrentX(), robot.getCurrentY(), 1, 0)) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkForTenRobots(int count, final List<RobotConfig> robots, int x, int y, int xMove, int yMove) {
        if (count >= 10) {
            return true;
        }

        final boolean found = robots.stream()
                .anyMatch(robot -> robot.getCurrentX() == x && robot.getCurrentY() == y);
        if (found) {
            return checkForTenRobots(count + 1, robots,  x + xMove, y + yMove, xMove, yMove);
        } else {
            return false;
        }
    }

    private static void printMap(final int second, final List<RobotConfig> robots) {
        System.out.println("Second: " + second);
        for (int y = 0; y < AREA_SIZE_Y; y++) {
            for (int x = 0; x < AREA_SIZE_X; x++) {
                final int finalX = x;
                final int finalY = y;
                final long count = robots.stream()
                        .filter(robot -> robot.getCurrentX() == finalX && robot.getCurrentY() == finalY)
                        .count();
                if (count == 0) {
                    System.out.print(".");
                } else if (count == 1) {
                    System.out.print("R");
                } else {
                    System.out.print(count);
                }
            }
            System.out.println();
        }
        System.out.println("-------");
    }

    private static class RobotConfig {

        private final int startX;
        private final int startY;

        private final int velocityX;
        private final int velocityY;

        private int currentX;

        private int currentY;

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

        public void setCurrentPosition(int currentX, int currentY) {
            this.currentX = currentX;
            this.currentY = currentY;
        }

        public int getCurrentX() {
            return currentX;
        }

        public int getCurrentY() {
            return currentY;
        }

    }
}
