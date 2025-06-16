import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Day6Part1 {

    public static Direction guardDirection = Direction.UP;

    private static List<List<Character>> getInput() throws IOException {
        Path path = Paths.get("day-06/finn/resource.txt");
        Scanner scanner = new Scanner(path);

        List<String> allLines = new ArrayList<>();
        while(scanner.hasNextLine()){
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
/*
        List<List<Character>> input = new ArrayList<>(List.of(
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

        moveGuard(guard, input);

        System.out.println("Result: " + countWalkedFields(input));
    }

    private static void moveGuard(final Point guard, final List<List<Character>> input) {
        markPointVisited(guard, input);
        Point nextPoint = nextPoint(guard, guardDirection);

        if (nextPoint.getX() < 0 || nextPoint.getX() > input.getFirst().size() - 1
                || nextPoint.getY() < 0 || nextPoint.getY() > input.size() - 1) {
            return;
        }

        while ((input.get(nextPoint.getY()).get(nextPoint.getX()) == '#')) {
            guardDirection = Direction.changeDirection(guardDirection);
            nextPoint = nextPoint(guard, guardDirection);

            if (nextPoint.getX() < 0 || nextPoint.getX() > input.getFirst().size() - 1
                    || nextPoint.getY() < 0 || nextPoint.getY() > input.size() - 1) {
                return;
            }
        }

        moveGuard(nextPoint, input);
    }

    private static Point nextPoint(final Point guard, final Direction direction) {
        return switch (direction) {
            case UP -> new Point(guard.getX(), guard.getY() - 1);
            case DOWN -> new Point(guard.getX(), guard.getY() + 1);
            case LEFT -> new Point(guard.getX() - 1, guard.getY());
            case RIGHT -> new Point(guard.getX() + 1, guard.getY());
        };
    }

    private static void markPointVisited(final Point point, final List<List<Character>> input) {
        input.get(point.getY()).set(point.getX(), 'X');
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

    private static int countWalkedFields(final List<List<Character>> input) {
        int count = 0;
        for (int y = 0; y < input.size(); y++) {
            for (int x = 0; x < input.get(y).size(); x++) {
                if (input.get(y).get(x) == 'X') {
                    count++;
                }
            }
        }
        return count;
    }

}
