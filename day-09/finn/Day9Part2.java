import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Day9Part2 {

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

        LinkedNode<FileInfo> lastElement = getInputRepresentation(input);
        LinkedNode<FileInfo> firstElement = getFirstElement(lastElement);
        System.out.println("Representation");
        reorderInputRepresentation(firstElement, lastElement);
        System.out.println("Reordered");

        List<Integer> reorderedResult = buildResultList(firstElement);

        long result = 0;
        for (int i = 0; i < reorderedResult.size(); i++) {
            if (reorderedResult.get(i) != null) {
                result += reorderedResult.get(i) * i;
            }
        }

        reorderedResult.forEach(System.out::print);
        System.out.println("Result:" + result);
    }

    private static List<Integer> buildResultList(LinkedNode<FileInfo> element) {
        List<Integer> result = new ArrayList<>();

        writeToList(element, result);
        do {
            element = element.getNext();
            writeToList(element, result);
        } while (element.hasNext());

        return result;
    }

    private static void writeToList(LinkedNode<FileInfo> element, List<Integer> result) {
        for (int i = 0; i < element.getValue().getSize(); i++) {
            result.add(element.getValue().getId());
        }
    }

    private static LinkedNode<FileInfo> getFirstElement(LinkedNode<FileInfo> element) {
        if(element.hasPre()) {
            return getFirstElement(element.getPre());
        }
        return element;
    }

    private static LinkedNode<FileInfo> getInputRepresentation(final String input) {
        int id = 0;
        LinkedNode<FileInfo> lastElement = new LinkedNode<>(null, null, new FileInfo(id, Character.getNumericValue(input.charAt(0))));
        id++;
        boolean isFiles = false;
        for (int i = 1; i < input.length(); i++) {
            if (isFiles) {
                lastElement.addAfter(new FileInfo(id, Character.getNumericValue(input.charAt(i))));
                lastElement = lastElement.getNext();
                id++;
            } else {
                lastElement.addAfter(new FileInfo(null, Character.getNumericValue(input.charAt(i))));
                lastElement = lastElement.getNext();
            }
            isFiles = !isFiles;
        }
        return lastElement;
    }

    private static void reorderInputRepresentation(LinkedNode<FileInfo> firstElement, LinkedNode<FileInfo> lastElement) {
        switchElementsIfAble(firstElement, lastElement);
        do {
            lastElement = lastElement.getPre();
            switchElementsIfAble(firstElement, lastElement);
        } while (lastElement.hasPre());
    }

    private static void switchElementsIfAble(LinkedNode<FileInfo> firstElement, LinkedNode<FileInfo> lastElement) {
        LinkedNode<FileInfo> elementToSwitch = findFirstSwitchableElement(firstElement, lastElement.getValue());
        if (elementToSwitch != null) {
           if (elementToSwitch.getValue().getSize() > lastElement.getValue().getSize()) {
               final FileInfo newFreeSpace = new FileInfo(null, elementToSwitch.getValue().getSize() - lastElement.getValue().getSize());
               elementToSwitch.getValue().setId(lastElement.getValue().getId());
               elementToSwitch.getValue().setSize(lastElement.getValue().getSize());
               elementToSwitch.addAfter(newFreeSpace);
               lastElement.getValue().setId(null);
            } else {
               elementToSwitch.getValue().setId(lastElement.getValue().getId());
               lastElement.getValue().setId(null);
            }
        }
    }

    private static LinkedNode<FileInfo> findFirstSwitchableElement(LinkedNode<FileInfo> firstElement, FileInfo fileToSwitch) {
        LinkedNode<FileInfo> pointer = firstElement;

        if (pointer.getValue().getId() == null && pointer.getValue().getSize() >= fileToSwitch.getSize()) {
            return pointer;
        }
        if (Objects.equals(pointer.getValue().getId(), fileToSwitch.getId())) {
            return null;
        }

        do {
            pointer = pointer.getNext();
            if (Objects.equals(pointer.getValue().getId(), fileToSwitch.getId())) {
                break;
            }

            if (pointer.getValue().getId() == null && pointer.getValue().getSize() >= fileToSwitch.getSize()) {
                return pointer;
            }
        } while (pointer.hasNext());

        return null;
    }

}
