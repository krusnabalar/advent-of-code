package krusna.day13;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day13 {
    List<String> allLines;
    List<Pattern> patterns = new ArrayList<>();

    public class Pattern {
        List<List<Character>> grid;
        List<List<Character>> rotatedGrid;

        private Pattern() {
            grid = new ArrayList<>();
            rotatedGrid = new ArrayList<>();
        }

        private void getCols() {
            for (int i = 0; i < grid.get(0).size(); i++) {
                List<Character> col = new ArrayList<>();
                for (int j = 0; j < grid.size(); j++) {
                    col.add(grid.get(j).get(i));
                }
                rotatedGrid.add(col);
            }
        }
    }

    void fillPatterns() {
        Pattern pattern = new Pattern();
        for (String line : allLines) {
            if (line.isEmpty()) {
                patterns.add(pattern);
                pattern = new Pattern();
                continue;
            }
            List<Character> row = new ArrayList<>();
            for (char c : line.toCharArray()) {
                row.add(c);
            }
            pattern.grid.add(row);
        }
        patterns.add(pattern);
    }

    boolean isValidMiddle(List<List<Character>> grid, int leftPos, int rightPos) {
        for (List<Character> row : grid) {
            int l = leftPos, r = rightPos;
            while (l < r) {
                if (row.get(l) != row.get(r)) {
                    return false;
                }
                l++;
                r--;
            }
        }

        return true;
    }

    int findMiddle(Pattern pattern, boolean checkRow) {
        List<List<Character>> grid = pattern.grid;
        if (!checkRow) {
            pattern.getCols();
            grid = pattern.rotatedGrid;
        }

        List<Character> row = grid.get(0);
        StringBuilder forward = new StringBuilder();
        forward.append(row.get(0));
        int lPtr = 0;
        int rPtr = 0;
        int n = row.size() - 1;
        while (lPtr < n - 1) {
            if (rPtr == n) {
                lPtr++;
                forward.deleteCharAt(0);
            } else {
                rPtr++;
                forward.append(row.get(rPtr));
            }
            if ((rPtr - lPtr) % 2 == 1 && row.get(lPtr) == row.get(rPtr)) {
                if (forward.toString().equals(forward.reverse().toString()) && isValidMiddle(grid, lPtr, rPtr)) {
                    return lPtr + (rPtr - lPtr) / 2 + 1;
                }
                forward.reverse();
            }
        }

        return 0;
    }

    int getSummary(Pattern pattern) {
        int horizontalLOR = findMiddle(pattern, true);
        int verticalLOR = findMiddle(pattern, false);
        return horizontalLOR + verticalLOR * 100;
    }

    void part1() {
        int totalSummary = 0;
        for (Pattern pattern : patterns) {
            totalSummary += getSummary(pattern);
        }
        System.out.printf("Part 1: %d\n", totalSummary);
    }

    void part2() {

    }

    public static void main(String... args) throws Exception {
        Day13 day13 = new Day13();

        day13.allLines = Files.readAllLines(Paths.get("./day13.txt"));
        day13.fillPatterns();
        day13.part1();
        day13.part2();
    }
}
