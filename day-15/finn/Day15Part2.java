import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day15Part2 {

    private static Roboter roboter;

    private static String getMovementInput() throws IOException {
        Path path = Paths.get("day-15/finn/movementRessource.txt");
        Scanner scanner = new Scanner(path);

        List<String> allLines = new ArrayList<>();
        while (scanner.hasNextLine()) {
            allLines.add(scanner.nextLine());
        }
        scanner.close();

        return allLines.stream().reduce("", String::concat);
    }

    private static List<List<Character>> getMapInput() throws IOException {
        Path path = Paths.get("day-15/finn/mapRessource.txt");
        Scanner scanner = new Scanner(path);

        List<String> allLines = new ArrayList<>();
        while (scanner.hasNextLine()) {
            allLines.add(scanner.nextLine());
        }
        scanner.close();

        return allLines.stream()
                .map(line -> line.chars().mapToObj((i) -> (char) i).toList())
                .toList();
    }

    public static void main(String[] args) throws IOException {

        /*List<List<Character>> map = new ArrayList<>(List.of(
                new ArrayList<>(List.of('#','#','#','#','#','#','#','#','#','#')),
                new ArrayList<>(List.of('#','.','.','O','.','.','O','.','O','#')),
                new ArrayList<>(List.of('#','.','.','.','.','.','.','O','.','#')),
                new ArrayList<>(List.of('#','.','O','O','.','.','O','.','O','#')),
                new ArrayList<>(List.of('#','.','.','O','@','.','.','O','.','#')),
                new ArrayList<>(List.of('#','O','#','.','.','O','.','.','.','#')),
                new ArrayList<>(List.of('#','O','.','.','O','.','.','O','.','#')),
                new ArrayList<>(List.of('#','.','O','O','.','O','.','O','O','#')),
                new ArrayList<>(List.of('#','.','.','.','.','O','.','.','.','#')),
                new ArrayList<>(List.of('#','#','#','#','#','#','#','#','#','#'))
        ));
*/
        List<List<Character>> map = getMapInput();

        List<List<Position>> convertedMap = buildMap(map);

        /*String movement = "<vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^" +
                "vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v" +
                "><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<" +
                "<<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^" +
                "^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><" +
                "^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^" +
                ">^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^" +
                "<><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>" +
                "^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>" +
                "v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^";
*/
        String movement = getMovementInput();
        List<MovementDirection> movementOrder = convertMovement(movement);

        for (MovementDirection direction : movementOrder) {
            roboter.move(direction, convertedMap);
            //printMap(convertedMap);
        }

        printMap(convertedMap);
        calculateResult(convertedMap);
    }

    private static void printMap(List<List<Position>> map) {
        for (List<Position> row : map) {
            for (Position position : row) {
                if (position instanceof Blocker) {
                    System.out.print('#');
                } else if (position instanceof EmptyPosition emptyPosition) {
                    if (roboter != null && roboter.getPosition().equals(emptyPosition)) {
                        System.out.print('@');
                    } else if (emptyPosition.hasWideBox()) {
                        if (emptyPosition.getWideBox().getLeftPosition().equals(emptyPosition)) {
                            System.out.print('[');
                        } else {
                            System.out.print(']');
                        }
                    } else if (emptyPosition.hasBox()) {
                        System.out.print('o');
                    } else {
                        System.out.print('.');
                    }
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void calculateResult(List<List<Position>> map) {
        int result = 0;
        for (List<Position> row : map) {
            for (int x = 0; x < row.size(); x++) {
                if (row.get(x) instanceof EmptyPosition emptyPosition && emptyPosition.hasWideBox()) {
                    if (emptyPosition.getWideBox().getLeftPosition().equals(emptyPosition)) {
                        result += 100 * emptyPosition.getY() + emptyPosition.getX();
                    }
                }
            }
        }
        System.out.println("Result: " + result);
    }

    private static List<MovementDirection> convertMovement(String movement) {
        List<MovementDirection> directions = new ArrayList<>();
        for (char move : movement.toCharArray()) {
            directions.add(switch (move) {
                case '<' -> MovementDirection.LEFT;
                case '>' -> MovementDirection.RIGHT;
                case '^' -> MovementDirection.UP;
                case 'v' -> MovementDirection.DOWN;
                default -> throw new IllegalArgumentException("Unknown movement character: " + move);
            });
        }
        return directions;
    }

    private static List<List<Position>> buildMap(List<List<Character>> map) {
        List<List<Position>> convertedMap = new ArrayList<>();

        for (int y = 0; y < map.size(); y++) {
            List<Position> convertedRow = new ArrayList<>();
            for (int x = 0; x < map.getFirst().size(); x++) {
                int xForWide = x * 2;
                switch (map.get(y).get(x)) {
                    case '#' -> {
                        convertedRow.add(new Blocker(xForWide, y));
                        convertedRow.add(new Blocker(xForWide + 1, y));
                    }
                    case '.' -> {
                        convertedRow.add(new EmptyPosition(xForWide, y, false));
                        convertedRow.add(new EmptyPosition(xForWide + 1, y, false));
                    }
                    case 'O' -> {
                        final EmptyPosition leftPosition = new EmptyPosition(xForWide, y, false);
                        final EmptyPosition rightPosition = new EmptyPosition(xForWide + 1, y, false);
                        convertedRow.add(leftPosition);
                        convertedRow.add(rightPosition);
                        final WideBox wideBox = new WideBox(rightPosition, leftPosition);
                        leftPosition.setWideBox(wideBox);
                        rightPosition.setWideBox(wideBox);
                    }
                    case '@' -> {
                        final EmptyPosition position = new EmptyPosition(xForWide, y, false);
                        convertedRow.add(position);
                        convertedRow.add(new EmptyPosition(xForWide + 1, y, false));
                        roboter = new Roboter(position);
                    }
                    default -> throw new IllegalArgumentException("Unknown position character: " + map.get(y).get(x));
                };
            }
            convertedMap.add(convertedRow);
        }
        if (roboter == null) {
            throw new IllegalStateException("No starting position for the robot found");
        }
        return convertedMap;
    }

}
