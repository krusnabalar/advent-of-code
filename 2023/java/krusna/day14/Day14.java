package krusna.day14;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day14 {

    int n, m;
    List<String> allLines = new ArrayList<>();
    char[][] grid;
    int NUM_CYCLES = 1_000_000_000;

    void fillGrid() {
        n = allLines.size();
        m = allLines.get(0).length();
        grid = new char[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                char c = allLines.get(i).charAt(j);
                grid[i][j] = c;
            }
        }
    }

    int getLoad() {
        int load = 0;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[j][i] == 'O') {
                    load += n - j;
                }
            }
        }

        return load;
    }

    void moveNorth() {
        for (int i = 0; i < m; i++) {
            int lastStoppingPoint = 0;
            for (int j = 0; j < n; j++) {
                if (grid[j][i] == 'O') {
                    if (lastStoppingPoint == j) {
                        lastStoppingPoint = j + 1;
                    } else {
                        grid[j][i] = '.';
                        grid[lastStoppingPoint][i] = 'O';
                        lastStoppingPoint++;
                    }
                } else if (grid[j][i] == '#') {
                    lastStoppingPoint = j + 1;
                }
            }
        }
    }

    void moveWest() {
        for (int i = 0; i < n; i++) {
            int lastStoppingPoint = 0;
            for (int j = 0; j < m; j++) {
                if (grid[i][j] == 'O') {
                    if (lastStoppingPoint == j) {
                        lastStoppingPoint = j + 1;
                    } else {
                        grid[i][j] = '.';
                        grid[i][lastStoppingPoint] = 'O';
                        lastStoppingPoint++;
                    }
                } else if (grid[i][j] == '#') {
                    lastStoppingPoint = j + 1;
                }
            }
        }
    }

    void moveSouth() {
        for (int i = 0; i < m; i++) {
            int lastStoppingPoint = n - 1;
            for (int j = n - 1; j >= 0; j--) {
                if (grid[j][i] == 'O') {
                    if (lastStoppingPoint == j) {
                        lastStoppingPoint = j - 1;
                    } else {
                        grid[j][i] = '.';
                        grid[lastStoppingPoint][i] = 'O';
                        lastStoppingPoint--;
                    }
                } else if (grid[j][i] == '#') {
                    lastStoppingPoint = j - 1;
                }
            }
        }
    }

    void moveEast() {
        for (int i = 0; i < n; i++) {
            int lastStoppingPoint = m - 1;
            for (int j = m - 1; j >= 0; j--) {
                if (grid[i][j] == 'O') {
                    if (lastStoppingPoint == j) {
                        lastStoppingPoint = j - 1;
                    } else {
                        grid[i][j] = '.';
                        grid[i][lastStoppingPoint] = 'O';
                        lastStoppingPoint--;
                    }
                } else if (grid[i][j] == '#') {
                    lastStoppingPoint = j - 1;
                }
            }
        }
    }

    String getStringFromGrid() {
        StringBuilder sb = new StringBuilder();
        for (char[] r : grid) {
            for (char c : r) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    int findLoadFromCycle() {
        List<String> listOfGridStates = new ArrayList<>();
        List<Integer> listOfLoads = new ArrayList<>();

        while (listOfGridStates.size() == new HashSet<>(listOfGridStates).size()) {
            moveNorth();
            moveWest();
            moveSouth();
            moveEast();
            listOfGridStates.add(getStringFromGrid());
            listOfLoads.add(getLoad());
        }

        int lastIdx = listOfGridStates.size() - 1;
        int firstIdx = listOfGridStates.indexOf(listOfGridStates.get(lastIdx));
        int offset = (NUM_CYCLES - 1 - firstIdx) % (lastIdx - firstIdx);
        return listOfLoads.get(firstIdx + offset);
    }

    void part1() {
        moveNorth();
        int res = getLoad();
        System.out.printf("Part 1: %d\n", res);
    }

    void part2() {
        int res = findLoadFromCycle();
        System.out.printf("Part 2: %d\n", res);
    }

    public static void main(String... args) throws Exception {
        Day14 day14 = new Day14();
        day14.allLines = Files.readAllLines(Paths.get("./day14.txt"));
        day14.fillGrid();
        day14.part1();
        day14.part2();
    }
}
