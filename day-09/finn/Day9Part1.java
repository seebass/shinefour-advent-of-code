import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Day9Part1 {

    private static String getInput() throws IOException {
        Path path = Paths.get("day-09/finn/resource.txt");
        Scanner scanner = new Scanner(path);

        String line = scanner.nextLine();
        scanner.close();

        return line;
    }

    public static void main(String[] args) throws IOException {
        //final String input = "2333133121414131402";
        final String input = getInput();

        List<Integer> inputRepresentation = getInputRepresentation(input);
        System.out.println("Representation");
        reorderInputRepresentation(inputRepresentation);
        System.out.println("Reordered");

        long result = 0;
        for (int i = 0; i < inputRepresentation.size(); i++) {
            if (inputRepresentation.get(i) != null) {
                result += inputRepresentation.get(i) * i;
            }
        }

        System.out.println("Result:" + result);
    }

    public static List<Integer> getInputRepresentation(final String input) {
        int id = 0;
        List<Integer> inputRepresentation = new LinkedList<>();
        boolean isFiles = true;
        for (int i = 0; i < input.length(); i++) {
            if(isFiles) {
                addCharXTimes(Character.getNumericValue(input.charAt(i)), inputRepresentation, id);
                id++;
            } else {
                addCharXTimes(Character.getNumericValue(input.charAt(i)), inputRepresentation, null);
            }
            isFiles = !isFiles;
        }
        return inputRepresentation;
    }

    private static void addCharXTimes(int x, List<Integer> inputRepresentation, Integer value) {
        for (int j = 1; j <= x; j++) {
            inputRepresentation.add(value);
        }
    }

    private static void reorderInputRepresentation(List<Integer> inputRepresentation) {
        int lastFilePosition = inputRepresentation.size() - 1;
        for (int i = 0; i < inputRepresentation.size(); i++) {
            if(lastFilePosition <= i) {
                break;
            }
            if (inputRepresentation.get(i) == null) {
                while (inputRepresentation.get(lastFilePosition) == null) {
                    lastFilePosition--;
                }
                inputRepresentation.set(i, inputRepresentation.get(lastFilePosition));
                inputRepresentation.set(lastFilePosition, null);
            }
        }
    }
}
