import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day8Part2 {

    private static List<List<Character>> getInput() throws IOException {
        Path path = Paths.get("day-08/finn/resource.txt");
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
                new ArrayList<>(List.of('.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.')),
                new ArrayList<>(List.of('.', '.', '.', '.', '.', '.', '.', '.', '0', '.', '.', '.')),
                new ArrayList<>(List.of('.', '.', '.', '.', '.', '0', '.', '.', '.', '.', '.', '.')),
                new ArrayList<>(List.of('.', '.', '.', '.', '.', '.', '.', '0', '.', '.', '.', '.')),
                new ArrayList<>(List.of('.', '.', '.', '.', '0', '.', '.', '.', '.', '.', '.', '.')),
                new ArrayList<>(List.of('.', '.', '.', '.', '.', '.', 'A', '.', '.', '.', '.', '.')),
                new ArrayList<>(List.of('.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.')),
                new ArrayList<>(List.of('.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.')),
                new ArrayList<>(List.of('.', '.', '.', '.', '.', '.', '.', '.', 'A', '.', '.', '.')),
                new ArrayList<>(List.of('.', '.', '.', '.', '.', '.', '.', '.', '.', 'A', '.', '.')),
                new ArrayList<>(List.of('.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.')),
                new ArrayList<>(List.of('.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'))
        ));*/

        List<List<Character>> input = getInput();

        final Map<Character, List<Day8Point>> antennas = getAntennas(input);
        final List<Day8Point> antinodes = new ArrayList<>();

        for (List<Day8Point> points : antennas.values()) {
            for (int i = 0; i < points.size(); i++) {
                for (int j = i + 1; j < points.size(); j++) {
                    calcAntinodes(points.get(i), points.get(j), input, antinodes);
                }
            }
        }

        System.out.println(antinodes.size());
    }

    private static boolean isPointUniqueAndValid(List<Day8Point> antinodes, Day8Point point, List<List<Character>> input) {
        return !antinodes.contains(point)
                && point.getX() < input.getFirst().size() && point.getX() >= 0
                && point.getY() < input.size() && point.getY() >= 0;
    }

    private static Map<Character, List<Day8Point>> getAntennas(List<List<Character>> input) {
        final Map<Character, List<Day8Point>> antennas = new HashMap<>();

        for (int x = 0; x < input.getFirst().size(); x++) {
            for (int y = 0; y < input.size(); y++) {
                Character mapValue = input.get(y).get(x);
                if (!mapValue.equals('.')) {
                    antennas.computeIfAbsent(mapValue, k -> new ArrayList<>());
                    antennas.get(mapValue).add(new Day8Point(x, y));
                }
            }
        }
        return antennas;
    }

    private static void calcAntinodes(final Day8Point point1, final Day8Point point2, final List<List<Character>> input, final List<Day8Point> antinodes) {
        final int x = point1.getX() - point2.getX();
        final int y = point1.getY() - point2.getY();

        if (isPointUniqueAndValid(antinodes, point1, input)) {
            antinodes.add(point1);
        }
        if (isPointUniqueAndValid(antinodes, point2, input)) {
            antinodes.add(point2);
        }

        for (int i = 1; i <= input.getFirst().size()
                && i <= input.size(); i++
        ) {
            Day8Point newPoint = new Day8Point(point1.getX() + i * x, point1.getY() + i * y);
            if (isPointUniqueAndValid(antinodes, newPoint, input)) {
                antinodes.add(newPoint);
            }
            newPoint = new Day8Point(point1.getX() - i * x, point1.getY() - i * y);
            if (isPointUniqueAndValid(antinodes, newPoint, input)) {
                antinodes.add(newPoint);
            }
            System.out.println("Alive:" + i);
        }
    }

}
