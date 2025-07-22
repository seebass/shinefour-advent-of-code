import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Day10Part1 {

    private static List<List<Integer>> getInput() throws IOException {
        Path path = Paths.get("day-10/finn/resource.txt");
        Scanner scanner = new Scanner(path);

        List<String> allLines = new ArrayList<>();
        while(scanner.hasNextLine()){
            allLines.add(scanner.nextLine());
        }
        scanner.close();

        return allLines.stream()
                .map(line ->
                        line.chars()
                                .mapToObj(Character::getNumericValue)
                                .collect(Collectors.toList())
                )
                .collect(Collectors.toList());
    }

    public static void main(String[] args) throws IOException {
        /*final List<List<Integer>> map = List.of(
                List.of(8, 9, 0, 1, 0, 1, 2, 3),
                List.of(7, 8, 1, 2, 1, 8, 7, 4),
                List.of(8, 7, 4, 3, 0, 9, 6, 5),
                List.of(9, 6, 5, 4, 9, 8, 7, 4),
                List.of(4, 5, 6, 7, 8, 9, 0, 3),
                List.of(3, 2, 0, 1, 9, 0, 1, 2),
                List.of(0, 1, 3, 2, 9, 8, 0, 1),
                List.of(1, 0, 4, 5, 6, 7, 3, 2)
        );*/

        final List<List<Integer>> map = getInput();

        final List<Day10Point> trailheads = getTrailheads(map);

        int pathCount = 0;

        for (final Day10Point trailhead : trailheads) {
            pathCount += walkTrailheadPaths(trailhead, map);
        }

        System.out.println("Total paths from trailheads: " + pathCount);
    }

    private static List<Day10Point> getTrailheads(List<List<Integer>> map) {
        final List<Day10Point> trailheads = new ArrayList<>();

        for (int x = 0; x < map.getFirst().size(); x++) {
            for (int y = 0; y < map.size(); y++) {
                Integer point = map.get(y).get(x);
                if (point == 0) {
                    trailheads.add(new Day10Point(x, y));
                }
            }
        }
        return trailheads;
    }

    private static int walkTrailheadPaths(final Day10Point trailhead, final List<List<Integer>> map) {
        final List<Day10Point> endPoints = new ArrayList<>();

        goUp(trailhead, map, endPoints);
        goDown(trailhead, map, endPoints);
        goLeft(trailhead, map, endPoints);
        goRight(trailhead, map, endPoints);

        return endPoints.size();
    }

    private static void goUp(final Day10Point position, final List<List<Integer>> map, final List<Day10Point> endPoints) {
        Integer value = map.get(position.getY()).get(position.getX());
        Day10Point newPosition = new Day10Point(position.getX(), position.getY() - 1);

        if (0 > newPosition.getY() || newPosition.getY() >= map.size()) {
            return; // Out of bounds
        }

        if (map.get(newPosition.getY()).get(newPosition.getX()) != value + 1) {
            return; // No valid path upwards
        }

        if (map.get(newPosition.getY()).get(newPosition.getX()) == 9) {
            if (endPoints.contains(newPosition)) {
                return; // Already counted this endpoint
            }
            endPoints.add(newPosition); // Reached the end of a path
        } else {
            goUp(newPosition, map, endPoints);
            goLeft(newPosition, map, endPoints);
            goRight(newPosition, map, endPoints);
        }
    }


    private static void goDown(final Day10Point position, final List<List<Integer>> map, final List<Day10Point> endPoints) {
        Integer value = map.get(position.getY()).get(position.getX());
        Day10Point newPosition = new Day10Point(position.getX(), position.getY() + 1);

        if (0 > newPosition.getY() || newPosition.getY() >= map.size()) {
            return; // Out of bounds
        }

        if (map.get(newPosition.getY()).get(newPosition.getX()) != value + 1) {
            return; // No valid path upwards
        }

        if (map.get(newPosition.getY()).get(newPosition.getX()) == 9) {
            if (endPoints.contains(newPosition)) {
                return; // Already counted this endpoint
            }
            endPoints.add(newPosition); // Reached the end of a path
        } else {
            goDown(newPosition, map, endPoints);
            goLeft(newPosition, map, endPoints);
            goRight(newPosition, map, endPoints);
        }
    }

    private static void goLeft(final Day10Point position, final List<List<Integer>> map, final List<Day10Point> endPoints) {
        Integer value = map.get(position.getY()).get(position.getX());
        Day10Point newPosition = new Day10Point(position.getX() - 1, position.getY());

        if (0 > newPosition.getX() || newPosition.getX() >= map.getFirst().size()) {
            return; // Out of bounds
        }

        if (map.get(newPosition.getY()).get(newPosition.getX()) != value + 1) {
            return; // No valid path upwards
        }

        if (map.get(newPosition.getY()).get(newPosition.getX()) == 9) {
            if (endPoints.contains(newPosition)) {
                return; // Already counted this endpoint
            }
            endPoints.add(newPosition); // Reached the end of a path
        } else {
            goUp(newPosition, map, endPoints);
            goDown(newPosition, map, endPoints);
            goLeft(newPosition, map, endPoints);
        }
    }

    private static void goRight(final Day10Point position, final List<List<Integer>> map, final List<Day10Point> endPoints) {
        Integer value = map.get(position.getY()).get(position.getX());
        Day10Point newPosition = new Day10Point(position.getX() + 1, position.getY());

        if (0 > newPosition.getX() || newPosition.getX() >= map.getFirst().size()) {
            return; // Out of bounds
        }

        if (map.get(newPosition.getY()).get(newPosition.getX()) != value + 1) {
            return; // No valid path upwards
        }

        if (map.get(newPosition.getY()).get(newPosition.getX()) == 9) {
            if (endPoints.contains(newPosition)) {
                return; // Already counted this endpoint
            }
            endPoints.add(newPosition); // Reached the end of a path
        } else {
            goUp(newPosition, map, endPoints);
            goDown(newPosition, map, endPoints);
            goRight(newPosition, map, endPoints);
        }
    }

}
