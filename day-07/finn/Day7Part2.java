import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Day7Part2 {

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
                List.of(190L, 10L, 19L),
                List.of(3267L, 81L, 40L, 27L),
                List.of(83L, 17L, 5L),
                List.of(156L, 15L, 6L),
                List.of(7290L, 6L, 8L, 6L, 15L),
                List.of(161011L, 16L, 10L, 13L),
                List.of(192L, 17L, 8L, 14L),
                List.of(21037L, 9L, 7L, 18L, 13L),
                List.of(292L, 11L, 6L, 16L, 20L)
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

    private static boolean multiply(final long result, long sum, List<Long> remainingValues) {
        if (remainingValues.isEmpty()) {
            return result == sum;
        }
        sum *= remainingValues.getFirst();
        remainingValues = remainingValues.subList(1, remainingValues.size());

        return add(result, sum, remainingValues) || multiply(result, sum, remainingValues) || concatination(result, sum, remainingValues);
    }

    private static boolean add(final long result, long sum, List<Long> remainingValues) {
        if (remainingValues.isEmpty()) {
            return result == sum;
        }
        sum += remainingValues.getFirst();
        remainingValues = remainingValues.subList(1, remainingValues.size());

        return add(result, sum, remainingValues) || multiply(result, sum, remainingValues) || concatination(result, sum, remainingValues);
    }

    private static boolean concatination(final long result, long sum, List<Long> remainingValues) {
        if (remainingValues.isEmpty()) {
            return result == sum;
        }
        sum = Long.parseLong(String.valueOf(sum).concat(String.valueOf(remainingValues.getFirst())));
        remainingValues = remainingValues.subList(1, remainingValues.size());

        return add(result, sum, remainingValues) || multiply(result, sum, remainingValues) || concatination(result, sum, remainingValues);
    }
}

