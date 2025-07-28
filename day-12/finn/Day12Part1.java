import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Day12Part1 {

    private static List<List<Character>> getInput() throws IOException {
        Path path = Paths.get("day-12/finn/resource.txt");
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

        /*List<List<Character>> input = List.of(
                List.of('R', 'R', 'R', 'R', 'I', 'I', 'C', 'C', 'F', 'F'),
                List.of('R', 'R', 'R', 'R', 'I', 'I', 'C', 'C', 'C', 'F'),
                List.of('V', 'V', 'R', 'R', 'R', 'C', 'C', 'F', 'F', 'F'),
                List.of('V', 'V', 'R', 'C', 'C', 'C', 'J', 'F', 'F', 'F'),
                List.of('V', 'V', 'V', 'V', 'C', 'J', 'J', 'C', 'F', 'E'),
                List.of('V', 'V', 'I', 'V', 'C', 'C', 'J', 'J', 'E', 'E'),
                List.of('V', 'V', 'I', 'I', 'I', 'C', 'J', 'J', 'E', 'E'),
                List.of('M', 'I', 'I', 'I', 'I', 'I', 'J', 'J', 'E', 'E'),
                List.of('M', 'I', 'I', 'I', 'S', 'I', 'J', 'E', 'E', 'E'),
                List.of('M', 'M', 'M', 'I', 'S', 'S', 'J', 'E', 'E', 'E')
        );*/
        List<List<Character>> input = getInput();


                List<Region> regions = findRegions(input);

        Long result = regions.stream()
                .map(region -> region.getPerimeter() * region.getFields())
                .reduce(0L, Long::sum);

        System.out.println("Result: " + result);
    }

    private static List<Region> findRegions(List<List<Character>> input) {
        List<Region> regions = new ArrayList<>();
        List<Day10Point> visitedPoints = new ArrayList<>();
        for (int y = 0; y < input.size(); y++) {
            for (int x = 0; x < input.getFirst().size(); x++) {
                if (!listContainsPoint(visitedPoints, x, y)) {
                    Region region = exploreRegion(input, y, x, visitedPoints);
                    regions.add(region);
                }
            }
        }
        return regions;
    }

    private static boolean listContainsPoint(List<Day10Point> visitedPoints, int x, int y) {
        return visitedPoints.stream().anyMatch(point -> point.getX() == x && point.getY() == y);
    }

    private static Region exploreRegion(List<List<Character>> input, int y, int x, List<Day10Point> visitedPoints) {
        Character selected = input.get(y).get(x);
        Region newRegion = new Region();
        visitedPoints.add(new Day10Point(x, y));
        newRegion.addField();

        exploreRight(input, y, x, selected, newRegion, visitedPoints);
        exploreLeft(input, y, x, selected, newRegion, visitedPoints);
        exploreUp(input, y, x, selected, newRegion, visitedPoints);
        exploreDown(input, y, x, selected, newRegion, visitedPoints);

        return newRegion;
    }

    private static void exploreRight(List<List<Character>> input, int y, int x, Character selectedChar, Region region, List<Day10Point> visitedPoints) {
        x = x + 1;

        if (x >= input.getFirst().size()) {
            region.addPerimeter();
            return;
        }

        if (!input.get(y).get(x).equals(selectedChar)) {
            region.addPerimeter();
            return;
        }

        if (listContainsPoint(visitedPoints, x, y)) {
            return;
        }

        visitedPoints.add(new Day10Point(x, y));
        region.addField();

        exploreRight(input, y, x, selectedChar, region, visitedPoints);
        exploreDown(input, y, x, selectedChar, region, visitedPoints);
        exploreUp(input, y, x, selectedChar, region, visitedPoints);
    }

    private static void exploreLeft(List<List<Character>> input, int y, int x, Character selectedChar, Region region, List<Day10Point> visitedPoints) {
        x = x - 1;

        if (x < 0) {
            region.addPerimeter();
            return;
        }

        if (!input.get(y).get(x).equals(selectedChar)) {
            region.addPerimeter();
            return;
        }

        if (listContainsPoint(visitedPoints, x, y)) {
            return;
        }

        visitedPoints.add(new Day10Point(x, y));
        region.addField();

        exploreLeft(input, y, x, selectedChar, region, visitedPoints);
        exploreDown(input, y, x, selectedChar, region, visitedPoints);
        exploreUp(input, y, x, selectedChar, region, visitedPoints);
    }

    private static void exploreUp(List<List<Character>> input, int y, int x, Character selectedChar, Region region, List<Day10Point> visitedPoints) {
        y = y - 1;

        if (y < 0) {
            region.addPerimeter();
            return;
        }

        if (!input.get(y).get(x).equals(selectedChar)) {
            region.addPerimeter();
            return;
        }

        if (listContainsPoint(visitedPoints, x, y)) {
            return;
        }

        visitedPoints.add(new Day10Point(x, y));
        region.addField();

        exploreLeft(input, y, x, selectedChar, region, visitedPoints);
        exploreRight(input, y, x, selectedChar, region, visitedPoints);
        exploreUp(input, y, x, selectedChar, region, visitedPoints);
    }

    private static void exploreDown(List<List<Character>> input, int y, int x, Character selectedChar, Region region, List<Day10Point> visitedPoints) {
        y = y + 1;

        if (y >= input.size()) {
            region.addPerimeter();
            return;
        }

        if (!input.get(y).get(x).equals(selectedChar)) {
            region.addPerimeter();
            return;
        }

        if (listContainsPoint(visitedPoints, x, y)) {
            return;
        }

        visitedPoints.add(new Day10Point(x, y));
        region.addField();

        exploreLeft(input, y, x, selectedChar, region, visitedPoints);
        exploreRight(input, y, x, selectedChar, region, visitedPoints);
        exploreDown(input, y, x, selectedChar, region, visitedPoints);
    }

    private static class Region {
        private long perimeter = 0;
        private long fields = 0;

        public long getPerimeter() {
            return perimeter;
        }

        public void addPerimeter() {
            this.perimeter++;
        }

        public long getFields() {
            return fields;
        }

        public void addField() {
            this.fields++;
        }

    }

}
