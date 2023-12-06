package krusna.day03;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day03 {
    private static void checkForGear(char[][] matrix, Map<List<Integer>, List<Integer>> adjacentNumsToGear, int r,
            int c,
            int val) {
        if (matrix[r][c] != '*')
            return;
        List<Integer> loc = List.of(r, c);
        List<Integer> adjacentNums = adjacentNumsToGear.getOrDefault(loc, new ArrayList<>());
        adjacentNums.add(val);
        adjacentNumsToGear.put(loc, adjacentNums);
    }

    private static int checkNeighbors(char[][] matrix, Set<Character> nums, int row, int colStart, int colEnd,
            Map<List<Integer>, List<Integer>> adjacentNumsToGear) {
        int val = 0;
        for (int i = colStart; i <= colEnd; i++) {
            val = (val * 10) + Character.getNumericValue(matrix[row][i]);
        }

        // left
        if (colStart != 0) {
            if (!nums.contains(matrix[row][colStart - 1])) {
                checkForGear(matrix, adjacentNumsToGear, row, colStart - 1, val);
                return val;
            }
            if (row != 0 && !nums.contains(matrix[row - 1][colStart - 1])) {
                checkForGear(matrix, adjacentNumsToGear, row - 1, colStart - 1, val);
                return val;
            }
            if (row != matrix.length - 1 && !nums.contains(matrix[row + 1][colStart - 1])) {
                checkForGear(matrix, adjacentNumsToGear, row + 1, colStart - 1, val);
                return val;
            }
        }
        // top and bottom
        for (int i = colStart; i <= colEnd; i++) {
            // check above curr element
            if (row != 0 && !nums.contains(matrix[row - 1][i])) {
                checkForGear(matrix, adjacentNumsToGear, row - 1, i, val);
                return val;
            }

            // check below of curr element
            if (row != matrix.length - 1 && !nums.contains(matrix[row + 1][i])) {
                checkForGear(matrix, adjacentNumsToGear, row + 1, i, val);
                return val;
            }
        }
        // right
        if (colEnd != matrix[0].length - 1) {
            if (!nums.contains(matrix[row][colEnd + 1])) {
                checkForGear(matrix, adjacentNumsToGear, row, colEnd + 1, val);
                return val;
            }
            if (row != 0 && !nums.contains(matrix[row - 1][colEnd + 1])) {
                checkForGear(matrix, adjacentNumsToGear, row - 1, colEnd + 1, val);
                return val;
            }
            if (row != matrix.length - 1 && !nums.contains(matrix[row + 1][colEnd + 1])) {
                checkForGear(matrix, adjacentNumsToGear, row + 1, colEnd + 1, val);
                return val;
            }
        }

        return 0;
    }

    public static void main(String... args) throws Exception {
        List<String> allLines = Files.readAllLines(Paths.get("./day03.txt"));
        int rows = allLines.size(), cols = allLines.get(0).length();
        char[][] matrix = new char[rows][cols];
        Set<Character> nums = Set.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
        Set<Character> numsWithDecimal = Set.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.');
        Map<List<Integer>, List<Integer>> adjacentNumsToGear = new HashMap<>();

        for (int row = 0; row < rows; row++) {
            char[] charArr = allLines.get(row).toCharArray();
            for (int col = 0; col < cols; col++) {
                matrix[row][col] = charArr[col];
            }
        }

        int resultP1 = 0, resultP2 = 0;
        int l = -1, r = -1;
        boolean traversingDigit = false;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

                if (nums.contains(matrix[row][col])) {
                    if (!traversingDigit) {
                        l = col;
                        traversingDigit = true;
                    }
                } else if (traversingDigit) {
                    r = col - 1;
                    traversingDigit = false;
                    resultP1 += checkNeighbors(matrix, numsWithDecimal, row, l, r, adjacentNumsToGear);
                    l = -1;
                    r = -1;
                }
            }
            if (traversingDigit)
                resultP1 += checkNeighbors(matrix, numsWithDecimal, row, l, cols - 1, adjacentNumsToGear);
            traversingDigit = false;
            l = -1;
            r = -1;
        }
        for (List<Integer> loc : adjacentNumsToGear.keySet()) {
            if (adjacentNumsToGear.get(loc).size() == 2) {
                resultP2 += adjacentNumsToGear.get(loc).get(0) * adjacentNumsToGear.get(loc).get(1);
            }
        }

        System.out.printf("part 1: %d\n", resultP1);
        System.out.printf("part 2: %d\n", resultP2);
    }
}
