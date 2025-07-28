import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day12Part2 {

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
                .map(region -> region.calculateSides() * region.getFields())
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
        int newX = x + 1;

        if (newX >= input.getFirst().size()) {
            region.addToRightBoarder(x, y);
            return;
        }

        if (!input.get(y).get(newX).equals(selectedChar)) {
            region.addToRightBoarder(x, y);
            return;
        }

        if (listContainsPoint(visitedPoints, newX, y)) {
            return;
        }

        visitedPoints.add(new Day10Point(newX, y));
        region.addField();

        exploreRight(input, y, newX, selectedChar, region, visitedPoints);
        exploreDown(input, y, newX, selectedChar, region, visitedPoints);
        exploreUp(input, y, newX, selectedChar, region, visitedPoints);
    }

    private static void exploreLeft(List<List<Character>> input, int y, int x, Character selectedChar, Region region, List<Day10Point> visitedPoints) {
        int newX = x - 1;

        if (newX < 0) {
            region.addToLeftBoarder(x, y);
            return;
        }

        if (!input.get(y).get(newX).equals(selectedChar)) {
            region.addToLeftBoarder(x, y);
            return;
        }

        if (listContainsPoint(visitedPoints, newX, y)) {
            return;
        }

        visitedPoints.add(new Day10Point(newX, y));
        region.addField();

        exploreLeft(input, y, newX, selectedChar, region, visitedPoints);
        exploreDown(input, y, newX, selectedChar, region, visitedPoints);
        exploreUp(input, y, newX, selectedChar, region, visitedPoints);
    }

    private static void exploreUp(List<List<Character>> input, int y, int x, Character selectedChar, Region region, List<Day10Point> visitedPoints) {
        int newY = y - 1;

        if (newY < 0) {
            region.addToUpBoarder(x, y);
            return;
        }

        if (!input.get(newY).get(x).equals(selectedChar)) {
            region.addToUpBoarder(x, y);
            return;
        }

        if (listContainsPoint(visitedPoints, x, newY)) {
            return;
        }

        visitedPoints.add(new Day10Point(x, newY));
        region.addField();

        exploreLeft(input, newY, x, selectedChar, region, visitedPoints);
        exploreRight(input, newY, x, selectedChar, region, visitedPoints);
        exploreUp(input, newY, x, selectedChar, region, visitedPoints);
    }

    private static void exploreDown(List<List<Character>> input, int y, int x, Character selectedChar, Region region, List<Day10Point> visitedPoints) {
        int newY = y + 1;

        if (newY >= input.size()) {
            region.addToDownBoarder(x, y);
            return;
        }

        if (!input.get(newY).get(x).equals(selectedChar)) {
            region.addToDownBoarder(x, y);
            return;
        }

        if (listContainsPoint(visitedPoints, x, newY)) {
            return;
        }

        visitedPoints.add(new Day10Point(x, newY));
        region.addField();

        exploreLeft(input, newY, x, selectedChar, region, visitedPoints);
        exploreRight(input, newY, x, selectedChar, region, visitedPoints);
        exploreDown(input, newY, x, selectedChar, region, visitedPoints);
    }

    private static class Region {
        private long fields = 0;

        private final Map<Integer, List<Integer>> upBorder = new HashMap<>();
        private final Map<Integer, List<Integer>> downBorder = new HashMap<>();
        private final Map<Integer, List<Integer>> leftBorder = new HashMap<>();
        private final Map<Integer, List<Integer>> rightBorder = new HashMap<>();

        public long calculateSides() {
            long sides = calculateSidesForDirection(this.upBorder);
            sides += calculateSidesForDirection(this.downBorder);
            sides += calculateSidesForDirection(this.leftBorder);
            sides += calculateSidesForDirection(this.rightBorder);
            return sides;
        }

        private long calculateSidesForDirection(Map<Integer, List<Integer>> border) {
            long sides = 0L;
            for (List<Integer> potentialSide : border.values()) {
                sides++;
                potentialSide.sort(Integer::compareTo);
                for (int i = 0; i < potentialSide.size() - 1; i++) {
                    if (potentialSide.get(i) + 1 != potentialSide.get(i + 1)) {
                        sides++;
                    }
                }
            }
            return sides;
        }

        public long getFields() {
            return fields;
        }

        public void addField() {
            this.fields++;
        }

        public void addToUpBoarder(int x, int y) {
            this.upBorder.computeIfAbsent(y, k -> new ArrayList<>());
            this.upBorder.get(y).add(x);
        }

        public void addToDownBoarder(int x, int y) {
            this.downBorder.computeIfAbsent(y, k -> new ArrayList<>());
            this.downBorder.get(y).add(x);
        }

        public void addToLeftBoarder(int x, int y) {
            this.leftBorder.computeIfAbsent(x, k -> new ArrayList<>());
            this.leftBorder.get(x).add(y);
        }

        public void addToRightBoarder(int x, int y) {
            this.rightBorder.computeIfAbsent(x, k -> new ArrayList<>());
            this.rightBorder.get(x).add(y);
        }

    }

}
