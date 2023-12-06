import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day05 {
    public static void main(String... args) throws Exception {
        List<String> allLines = Files.readAllLines(Paths.get("../testInput/day5.txt")),
                stacks = new ArrayList<String>();
        int numStacks = 0, lineTracker = 1;
        // count number of stacks and save lines storing crate positions
        configureStacks: for (String line : allLines) {
            lineTracker++;
            if (line.charAt(1) == '1') {
                numStacks = Integer.parseInt(line.substring(line.length() - 1));
                break configureStacks;
            } else {
                stacks.add(line);
            }
        }

        // if no stacks found, input is invalid or empty, end early
        if (numStacks == 0) {
            System.err.println("did not find any stacks");
            System.exit(0);
        }

        // fill stacks with appropriate crates in correct order
        @SuppressWarnings("unchecked")
        Stack<Character>[] stackList = new Stack[numStacks];
        for (int i = 0; i < numStacks; i++)
            stackList[i] = new Stack<Character>();
        int firstCratePos = 1, distanceBetweenCrates = 4;
        for (int i = stacks.size() - 1; i >= 0; i--) {
            // get line
            String stackLine = stacks.get(i);
            int lineLength = stackLine.length();
            // for each stack in stackList, check if crate exists
            for (int j = firstCratePos; j < lineLength; j += distanceBetweenCrates) {
                if (stackLine.charAt(j) != ' ') {
                    stackList[(j - firstCratePos) / distanceBetweenCrates].add(stackLine.charAt(j));
                }
            }
        }

        // move crates as per instructions
        boolean part1 = true;
        int cratesToMoveIdx = 1, stackFromIdx = 3, stackToIdx = 5;
        for (int i = lineTracker; i < allLines.size(); i++) {
            String[] lineSplit = allLines.get(i).split(" ");
            int cratesToMove = Integer.parseInt(lineSplit[cratesToMoveIdx]);
            int stackFrom = Integer.parseInt(lineSplit[stackFromIdx]) - 1;
            int stackTo = Integer.parseInt(lineSplit[stackToIdx]) - 1;

            // execute single instruction moving crates cratesToMove
            // from stackFrom to stackTo
            Stack<Character> from = stackList[stackFrom];
            Stack<Character> to = stackList[stackTo];
            StringBuilder crateList = new StringBuilder();
            for (int j = 0; j < cratesToMove; j++) {
                crateList.append(from.pop());
            }
            if (part1) {
                for (int j = 0; j < cratesToMove; j++) {
                    to.add(crateList.charAt(j));
                }
            } else {
                for (int j = cratesToMove - 1; j >= 0; j--) {
                    to.add(crateList.charAt(j));
                }
            }
        }

        StringBuilder result = new StringBuilder();
        for (Stack<Character> stack : stackList) {
            result.append(stack.peek());
        }

        // switch part1 boolean to true;
        System.out.printf("part 1: %s\n", result.toString());
        // switch part1 boolean to false;
        System.out.printf("part 2: %d\n", result.toString());
    }
}
