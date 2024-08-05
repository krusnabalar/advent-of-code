package krusna.day21;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day21 {
    private static final int[] rowDirections = {-1, 0, 1, 0};
    private static final int[] colDirections = {0, 1, 0, -1};
    List<String> allLines;
    Set<Pos> currentPositions = new HashSet<>();
    Pos start;
    int squareSize;
    char[][] grid;

    void setup() {
        squareSize = allLines.size();
        grid = new char[squareSize][squareSize];
        for (int i = 0; i < squareSize; i++) {
            grid[i] = allLines.get(i).toCharArray();
            for (int j = 0; j < squareSize; j++) {
                if (grid[i][j] == 'S') {
                    start = new Pos(i, j);
                    grid[i][j] = '.';
                }
            }
        }
        currentPositions.add(start);
    }

    long countReachablePlots(long steps, int part) {
        Map<Pos, Integer> seen = new HashMap<>();
        Queue<Pos> queue = new LinkedList<>();
        queue.offer(start);
        seen.put(start, 0);
        long even = 0, odd = 0;

        while (!queue.isEmpty()) {
            Pos current = queue.poll();
            int distance = seen.get(current);
            if (distance % 2 == 0) {
                even++;
            } else {
                odd++;
            }

            if (distance == steps) continue;

            for (int dir = 0; dir < 4; dir++) {
                int newRow = current.row + rowDirections[dir];
                int newCol = current.col + colDirections[dir];
                Pos next = new Pos(newRow, newCol);
                if (!isValidPos(newRow, newCol, part)) {
                    continue;
                }

                if (!seen.containsKey(next)) {
                    seen.put(next, distance + 1);
                    queue.offer(next);
                }
            }
        }

        return steps % 2 == 0 ? even : odd;
    }

    long quadraticPlotCount(long steps) {
        long size = squareSize, sizeByTwo = size / 2;

        long f0 = countReachablePlots(sizeByTwo, 2);
        long f1 = countReachablePlots(sizeByTwo + size, 2);
        long f2 = countReachablePlots(sizeByTwo + (2 * size), 2);

        long a = (f2 - (2 * f1) + f0) / 2;
        long b = f1 = f0 - a;
        long c = f0;

        long x = (steps - sizeByTwo) / size;

        return (a * x * x) + (b * x) + c;
    }

    boolean isValidPos(int row, int col, int part) {
        if (part == 1) {
            return (row >= 0 && row < squareSize && col >= 0 && col < squareSize && grid[row][col] == '.');
        } else {
            Pos posModulus = modulusPos(row, col);
            return grid[posModulus.row][posModulus.col] == '.';
        }
    }

    Pos modulusPos(int row, int col) {
        int rowModulus = ((row % squareSize) + squareSize) % squareSize;
        int colModulus = ((col % squareSize) + squareSize) % squareSize;
        return new Pos(rowModulus, colModulus);
    }

    void part1() {
        System.out.printf("Part 1: %d\n", countReachablePlots(64, 1));
    }

    void part2() {
        System.out.printf("Part 2: %d\n", quadraticPlotCount(26501365));
    }

    public static void main(String... args) throws Exception {
        Day21 day21 = new Day21();
        day21.allLines = Files.readAllLines(Paths.get("./day21.txt"));
        day21.setup();
        day21.part1();
        day21.part2();
    }
}

class Pos {
    int row, col;

    public Pos(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pos pos = (Pos) o;
        return row == pos.row && col == pos.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
