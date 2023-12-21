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

    boolean isValidMiddle(List<List<Character>> grid, int leftPos, int rightPos, int part) {
        int numSmudges = 0;
        for (List<Character> row : grid) {
            int l = leftPos, r = rightPos;
            while (l < r) {
                if (row.get(l) != row.get(r)) {
                    if (part == 1) {
                        return false;
                    }
                    numSmudges++;
                }
                l++;
                r--;
            }
        }
        if (part == 1) {
            return true;
        } else {
            return numSmudges == 1;
        }
    }

    boolean checkSymmetry(String s1, String s2, int part) {
        int numSmudges = 0;
        int len1 = s1.length(), len2 = s2.length();
        if (len1 != len2)
            return false;
        for (int i = 0; i < len1; i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                if (part == 1) {
                    return false;
                }
                numSmudges++;
                if (numSmudges > 2) {
                    return false;
                }
            }
        }
        return true;
    }

    int findMiddle(Pattern pattern, boolean checkRow, int part) {
        List<List<Character>> grid;
        if (checkRow) {
            grid = pattern.grid;
        } else {
            pattern.getCols();
            grid = pattern.rotatedGrid;
        }

        List<Character> row = grid.get(0);
        StringBuilder forward = new StringBuilder(row.size()).append(row.get(0));
        int lPtr = 0, rPtr = 0, n = row.size() - 1;
        while (lPtr < n - 1) {
            if (rPtr == n) {
                lPtr++;
                forward.deleteCharAt(0);
            } else {
                rPtr++;
                forward.append(row.get(rPtr));
            }
            if ((rPtr - lPtr) % 2 == 1) {
                if (checkSymmetry(forward.toString(), forward.reverse().toString(), part)
                        && isValidMiddle(grid, lPtr, rPtr, part)) {
                    return lPtr + (rPtr - lPtr) / 2 + 1;
                }
                forward.reverse();
            }
        }

        return 0;
    }

    void part1() {
        int totalSummary = 0;
        for (Pattern pattern : patterns) {
            int lineOfReflection = findMiddle(pattern, true, 1);
            if (lineOfReflection == 0) {
                lineOfReflection = findMiddle(pattern, false, 1) * 100;
            }
            totalSummary += lineOfReflection;
        }
        System.out.printf("Part 1: %d\n", totalSummary);
    }

    void part2() {
        int totalSummary = 0;
        for (Pattern pattern : patterns) {
            int lineOfReflection = findMiddle(pattern, true, 2);
            if (lineOfReflection == 0) {
                lineOfReflection = findMiddle(pattern, false, 2) * 100;
            }
            totalSummary += lineOfReflection;
        }
        System.out.printf("Part 2: %d\n", totalSummary);
    }

    public static void main(String... args) throws Exception {
        Day13 day13 = new Day13();

        day13.allLines = Files.readAllLines(Paths.get("./day13.txt"));
        day13.fillPatterns();
        day13.part2();
        day13.part1();
    }
}
