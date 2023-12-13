package krusna.day11;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day11 {
    List<String> allLines;
    List<int[]> galaxyPos = new ArrayList<>();
    List<int[]> galaxyPosPartTwo = new ArrayList<>();
    int galaxyCount;
    int EXPANSION_FACTOR = 2;
    int EXPANSION_FACTOR_PART_TWO = 1_000_000;

    public void expandImage() {
        int[] colContainsGalaxies = new int[allLines.get(0).length()];
        int rows = allLines.size();
        int cols = allLines.get(0).length();
        galaxyCount = 1;
        int rowIndex = 0, rowIndexPartTwo = 0;

        for (int i = 0; i < rows; i++) {
            boolean rowContainsGalaxy = false;
            for (int j = 0; j < cols; j++) {
                int pixel = allLines.get(i).charAt(j) == '#' ? galaxyCount++ : 0;
                if (pixel > 0) {
                    galaxyPos.add(new int[] { rowIndex, j });
                    galaxyPosPartTwo.add(new int[] { rowIndexPartTwo, j });
                    colContainsGalaxies[j] = 1;
                    rowContainsGalaxy = true;
                }
            }
            if (!rowContainsGalaxy) {
                rowIndex += EXPANSION_FACTOR - 1;
                rowIndexPartTwo += EXPANSION_FACTOR_PART_TWO - 1;
            }
            rowIndex++;
            rowIndexPartTwo++;
        }
        // update column indices based on respective expansions
        for (int i = 0; i < galaxyCount - 1; i++) {
            int colPos = galaxyPos.get(i)[1];
            for (int j = 0; j < colPos; j++) {
                if (colContainsGalaxies[j] == 0) {
                    galaxyPos.get(i)[1] += EXPANSION_FACTOR - 1;
                    galaxyPosPartTwo.get(i)[1] += EXPANSION_FACTOR_PART_TWO - 1;
                }
            }
        }
    }

    public long getShortestPath(int[] g1, int[] g2) {
        return Math.abs(g1[0] - g2[0]) + Math.abs(g1[1] - g2[1]);
    }

    public long sumOfShortestPaths(List<int[]> galaxyPositions) {
        long sum = 0;
        for (int i = 0; i < galaxyPositions.size(); i++) {
            for (int j = i + 1; j < galaxyPositions.size(); j++) {
                sum += getShortestPath(galaxyPositions.get(i), galaxyPositions.get(j));
            }
        }

        return sum;
    }

    public void part1() {
        long sumOfShortestPaths = sumOfShortestPaths(galaxyPos);

        System.out.printf("Part 1: %d\n", sumOfShortestPaths);
    }

    public void part2() {
        long sumOfShortestPaths = sumOfShortestPaths(galaxyPosPartTwo);

        System.out.printf("Part 2: %d\n", sumOfShortestPaths);
    }

    public static void main(String... args) throws Exception {
        Day11 day11 = new Day11();
        day11.allLines = Files.readAllLines(Paths.get("./day11.txt"));
        day11.expandImage();

        day11.part1();
        day11.part2();
    }
}
