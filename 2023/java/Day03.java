/*

--- Day 3: Gear Ratios ---

You and the Elf eventually reach a gondola lift station; he says the gondola lift will take you up to the water source, but this is as far as he can bring you. You go inside.

It doesn't take long to find the gondolas, but there seems to be a problem: they're not moving.

"Aaah!"

You turn around to see a slightly-greasy Elf with a wrench and a look of surprise. "Sorry, I wasn't expecting anyone! The gondola lift isn't working right now; it'll still be a while before I can fix it." You offer to help.

The engineer explains that an engine part seems to be missing from the engine, but nobody can figure out which one. If you can add up all the part numbers in the engine schematic, it should be easy to work out which part is missing.

The engine schematic (your puzzle input) consists of a visual representation of the engine. There are lots of numbers and symbols you don't really understand, but apparently any number adjacent to a symbol, even diagonally, is a "part number" and should be included in your sum. (Periods (.) do not count as a symbol.)

Here is an example engine schematic:

467..114..
...*......
..35..633.
......#...
617*......
.....+.58.
..592.....
......755.
...$.*....
.664.598..

In this schematic, two numbers are not part numbers because they are not adjacent to a symbol: 114 (top right) and 58 (middle right). Every other number is adjacent to a symbol and so is a part number; their sum is 4361.

Of course, the actual engine schematic is much larger. What is the sum of all of the part numbers in the engine schematic?

Your puzzle answer was 528799.
--- Part Two ---

The engineer finds the missing part and installs it in the engine! As the engine springs to life, you jump in the closest gondola, finally ready to ascend to the water source.

You don't seem to be going very fast, though. Maybe something is still wrong? Fortunately, the gondola has a phone labeled "help", so you pick it up and the engineer answers.

Before you can explain the situation, she suggests that you look out the window. There stands the engineer, holding a phone in one hand and waving with the other. You're going so slowly that you haven't even left the station. You exit the gondola.

The missing part wasn't the only issue - one of the gears in the engine is wrong. A gear is any * symbol that is adjacent to exactly two part numbers. Its gear ratio is the result of multiplying those two numbers together.

This time, you need to find the gear ratio of every gear and add them all up so that the engineer can figure out which gear needs to be replaced.

Consider the same engine schematic again:

467..114..
...*......
..35..633.
......#...
617*......
.....+.58.
..592.....
......755.
...$.*....
.664.598..

In this schematic, there are two gears. The first is in the top left; it has part numbers 467 and 35, so its gear ratio is 16345. The second gear is in the lower right; its gear ratio is 451490. (The * adjacent to 617 is not a gear because it is only adjacent to one part number.) Adding up all of the gear ratios produces 467835.

What is the sum of all of the gear ratios in your engine schematic?

Your puzzle answer was 84907174.

Both parts of this puzzle are complete! They provide two gold stars: **

*/

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
        List<String> allLines = Files.readAllLines(Paths.get("./input/day03.txt"));
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
