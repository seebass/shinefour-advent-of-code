import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Day7Part1 {

    private static List<List<Long>> getInput() throws IOException {
        Path path = Paths.get("day-07/finn/resource.txt");
        Scanner scanner = new Scanner(path);

        List<String> allLines = new ArrayList<>();
        while (scanner.hasNextLine()) {
            allLines.add(scanner.nextLine());
        }
        scanner.close();

        return allLines.stream()
                .map(line ->
                        Arrays.stream(line.replace(":", "")
                                .split(" "))
                                .map(Long::parseLong)
                                .collect(Collectors.toList())
                )
                .collect(Collectors.toList());
    }

    public static void main(String[] args) throws IOException {

        /*List<List<Long>> input = List.of(
                List.of(190, 10, 19),
                List.of(3267, 81, 40, 27),
                List.of(83, 17, 5),
                List.of(156, 15, 6),
                List.of(7290, 6, 8, 6, 15),
                List.of(161011, 16, 10, 13),
                List.of(192, 17, 8, 14),
                List.of(21037, 9, 7, 18, 13),
                List.of(292, 11, 6, 16, 20)
        );*/

        List<List<Long>> input = getInput();

        long sum = 0;

        for (List<Long> formel : input) {
            long result = formel.getFirst();
            List<Long> parameter = formel.subList(1, formel.size());
            if (calc(result, parameter)) {
                sum += result;
            }
        }

        System.out.println("Summe: " + sum);
    }

    private static boolean calc(final long result, List<Long> remainingValues) {
        if (remainingValues.isEmpty()) {
            return false;
        }

        return add(result, 0, remainingValues);
    }

    private static boolean multiply(final long result, long sum,  List<Long> remainingValues) {
        if (remainingValues.isEmpty()) {
            return result == sum;
        }
        sum *= remainingValues.getFirst();
        remainingValues = remainingValues.subList(1, remainingValues.size());

        return add(result, sum, remainingValues) || multiply(result, sum, remainingValues);
    }

    private static boolean add(final long result, long sum, List<Long> remainingValues) {
        if (remainingValues.isEmpty()) {
            return result == sum;
        }
        sum += remainingValues.getFirst();
        remainingValues = remainingValues.subList(1, remainingValues.size());

        return add(result, sum, remainingValues) || multiply(result, sum, remainingValues);
    }
}
