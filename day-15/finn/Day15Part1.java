import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day15Part1 {

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
        ));*/

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
        }

        calculateResult(convertedMap);
    }

    private static void calculateResult(List<List<Position>> map) {
        int result = 0;
        for (List<Position> row : map) {
            for (Position position : row) {
                if (position instanceof EmptyPosition emptyPosition && emptyPosition.hasBox()) {
                    result += 100 * emptyPosition.getY() + emptyPosition.getX();
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
                convertedRow.add(convertToPosition(map.get(y).get(x), x, y));
            }
            convertedMap.add(convertedRow);
        }
        if (roboter == null) {
            throw new IllegalStateException("No starting position for the robot found");
        }
        return convertedMap;
    }

    private static Position convertToPosition(char positionChar, int x, int y) {
        return switch (positionChar) {
            case '#' -> new Blocker(x, y);
            case '.' -> new EmptyPosition(x, y, false);
            case 'O' -> new EmptyPosition(x, y, true);
            case '@' -> createRobot(x, y);
            default -> throw new IllegalArgumentException("Unknown position character: " + positionChar);
        };
    }

    private static EmptyPosition createRobot(int x, int y) {
        final EmptyPosition position = new EmptyPosition(x, y, false);
        roboter = new Roboter(position);
        return position;
    }

}
