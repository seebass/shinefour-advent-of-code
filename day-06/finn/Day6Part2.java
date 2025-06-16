import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Day6Part2 {
    public static Direction guardDirection = Direction.UP;

    private static List<List<Character>> getInput() throws IOException {
        Path path = Paths.get("day-06/finn/resource.txt");
        Scanner scanner = new Scanner(path);

        List<String> allLines = new ArrayList<>();
        while (scanner.hasNextLine()) {
            allLines.add(scanner.nextLine());
        }
        scanner.close();

        return allLines.stream()
                .map(line ->
                        line.chars()
                                .mapToObj(c -> (char) c)
                                .collect(Collectors.toList())
                )
                .collect(Collectors.toList());
    }

    public static void main(String[] args) throws IOException {

        /*List<List<Character>> input = new ArrayList<>(List.of(
                new ArrayList<>(List.of('.', '.', '.', '.', '#', '.', '.', '.', '.', '.')),
                new ArrayList<>(List.of('.', '.', '.', '.', '.', '.', '.', '.', '.', '#')),
                new ArrayList<>(List.of('.', '.', '.', '.', '.', '.', '.', '.', '.', '.')),
                new ArrayList<>(List.of('.', '.', '#', '.', '.', '.', '.', '.', '.', '.')),
                new ArrayList<>(List.of('.', '.', '.', '.', '.', '.', '.', '#', '.', '.')),
                new ArrayList<>(List.of('.', '.', '.', '.', '.', '.', '.', '.', '.', '.')),
                new ArrayList<>(List.of('.', '#', '.', '.', '^', '.', '.', '.', '.', '.')),
                new ArrayList<>(List.of('.', '.', '.', '.', '.', '.', '.', '.', '#', '.')),
                new ArrayList<>(List.of('#', '.', '.', '.', '.', '.', '.', '.', '.', '.')),
                new ArrayList<>(List.of('.', '.', '.', '.', '.', '.', '#', '.', '.', '.'))
        ));*/

        List<List<Character>> input = getInput();

        Point guard = findGuard(input);

        moveGuard(new Point(guard.getX(), guard.getY()), input, guardDirection);

        input.get(guard.getY()).set(guard.getX(), '^');
        int newObstaclesFound = 0;
        for (int y = 0; y < input.size(); y++) {
            for (int x = 0; x < input.get(y).size(); x++) {
                if (input.get(y).get(x) == 'X') {
                    input.get(y).set(x, '#');
                    if (isLoop(new Point(guard.getX(), guard.getY()), input, guardDirection)) {
                        newObstaclesFound++;
                    }
                    input.get(y).set(x, 'X');
                }
            }
        } //1586 ist korrekt

        System.out.print("Result: " + newObstaclesFound);
    }

    private static void moveGuard(final Point guard, final List<List<Character>> input, Direction direction) {
        markPointVisited(guard, input);
        Point nextPoint = nextPoint(guard, direction);

        if (nextPoint.getX() < 0 || nextPoint.getX() > input.getFirst().size() - 1
                || nextPoint.getY() < 0 || nextPoint.getY() > input.size() - 1) {
            return;
        }

        while ((input.get(nextPoint.getY()).get(nextPoint.getX()) == '#')) {
            direction = Direction.changeDirection(direction);
            nextPoint = nextPoint(guard, direction);

            if (nextPoint.getX() < 0 || nextPoint.getX() > input.getFirst().size() - 1
                    || nextPoint.getY() < 0 || nextPoint.getY() > input.size() - 1) {
                return;
            }
        }

        moveGuard(nextPoint, input, direction);
    }

    private static void markPointVisited(final Point point, final List<List<Character>> input) {
        input.get(point.getY()).set(point.getX(), 'X');
    }

    private static boolean isLoop(Point testPoint, List<List<Character>> input, Direction direction) {
        List<PointWithDirection> alreadyVisited = new ArrayList<>();

        alreadyVisited.add(new PointWithDirection(testPoint.getX(), testPoint.getY(), direction));

        while (true) {
            Point nextPoint = nextPoint(testPoint, direction);

            if (nextPoint.getX() < 0 || nextPoint.getX() > input.getFirst().size() - 1
                    || nextPoint.getY() < 0 || nextPoint.getY() > input.size() - 1) {
                return false;
            }

            while ((input.get(nextPoint.getY()).get(nextPoint.getX()) == '#')) {
                direction = Direction.changeDirection(direction);
                nextPoint = nextPoint(testPoint, direction);

                if (nextPoint.getX() < 0 || nextPoint.getX() > input.getFirst().size() - 1
                        || nextPoint.getY() < 0 || nextPoint.getY() > input.size() - 1) {
                    return false;
                }
            }

            PointWithDirection nextPointWithDirection = new PointWithDirection(nextPoint.getX(), nextPoint.getY(), direction);
            if (alreadyVisited.stream().anyMatch(alreadyVisitedPoint -> alreadyVisitedPoint.equals(nextPointWithDirection))) {
                return true;

            }

            testPoint = nextPoint;
            alreadyVisited.add(new PointWithDirection(testPoint.getX(), testPoint.getY(), direction));
        }
    }

    private static Point nextPoint(final Point guard, final Direction direction) {
        return switch (direction) {
            case UP -> new Point(guard.getX(), guard.getY() - 1);
            case DOWN -> new Point(guard.getX(), guard.getY() + 1);
            case LEFT -> new Point(guard.getX() - 1, guard.getY());
            case RIGHT -> new Point(guard.getX() + 1, guard.getY());
        };
    }

    private static Point findGuard(final List<List<Character>> input) {
        for (int y = 0; y < input.size(); y++) {
            for (int x = 0; x < input.get(y).size(); x++) {
                if (input.get(y).get(x) == '^') {
                    guardDirection = Direction.UP;
                    return new Point(x, y);
                }
                if (input.get(y).get(x) == '<') {
                    guardDirection = Direction.LEFT;
                    return new Point(x, y);
                }
                if (input.get(y).get(x) == '>') {
                    guardDirection = Direction.RIGHT;
                    return new Point(x, y);
                }
                if (input.get(y).get(x) == 'V') {
                    guardDirection = Direction.DOWN;
                    return new Point(x, y);
                }
            }
        }
        throw new IllegalArgumentException("Guard not found in the input.");
    }

}
