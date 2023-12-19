package krusna.day12;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day12 {
    List<String> allLines;
    List<RowOfSprings> gridOfSprings = new ArrayList<>();
    List<RowOfSprings> gridOfSpringsPart2 = new ArrayList<>();
    HashMap<RowOfSprings, Long> memo;
    int NUM_FOLDS = 5;
    int PRIME = 31;

    public String unfoldRow(String row) {
        StringBuilder rowSB = new StringBuilder(row);
        for (int i = 0; i < NUM_FOLDS - 1; i++) {
            rowSB.append('?');
            rowSB.append(row);
        }

        return rowSB.toString();
    }

    public String unfoldCounts(String counts) {
        StringBuilder countsSB = new StringBuilder(counts);
        for (int i = 0; i < NUM_FOLDS - 1; i++) {
            countsSB.append(",");
            countsSB.append(counts);
        }

        return countsSB.toString();
    }

    public void fillGridOfSprings() {
        for (String line : allLines) {
            String row = line.split(" ")[0];
            String counts = line.split(" ")[1];
            String rowPart2 = unfoldRow(row);
            String countsPart2 = unfoldCounts(counts);

            gridOfSprings.add(new RowOfSprings((row).toCharArray(),
                    Arrays.stream(counts.split(",")).map(Integer::valueOf).toList(), 0, 0, 0));
            gridOfSpringsPart2.add(new RowOfSprings((rowPart2).toCharArray(),
                    Arrays.stream(countsPart2.split(",")).map(Integer::valueOf).toList(), 0, 0, 0));
        }
    }

    public void part1() {
        long sum = 0;
        for (RowOfSprings row : gridOfSprings) {
            sum += row.countPossibilities();
            memo.clear();
        }
        System.out.printf("Part 1: %d\n", sum);
    }

    public void part2() {
        long sum = 0;
        for (RowOfSprings row : gridOfSpringsPart2) {
            sum += row.countPossibilities();
            memo.clear();
        }
        System.out.printf("Part 2: %d\n", sum);
    }

    public static void main(String[] args) throws Exception {
        Day12 day12 = new Day12();

        day12.allLines = Files.readAllLines(Paths.get("./day12.txt"));
        day12.memo = new HashMap<>();
        day12.fillGridOfSprings();
        day12.part1();
        day12.part2();
    }

    public class RowOfSprings {
        char[] row;
        int rowIdx;
        List<Integer> counts;
        int countsIdx;
        int consecutiveDamaged;

        private RowOfSprings(char[] row, List<Integer> counts, int consecutiveDamaged, int rowIdx, int countsIdx) {
            this.row = row;
            this.counts = counts;
            this.consecutiveDamaged = consecutiveDamaged;
            this.rowIdx = rowIdx;
            this.countsIdx = countsIdx;
        }

        // recursive dp with memoization
        public long countPossibilities() {
            if (memo.containsKey(this)) {
                return memo.get(this);
            }

            long answer = 0;
            if (rowIdx == row.length) {
                // check if last count of damaged springs has been considered
                if ((countsIdx == counts.size()
                        && consecutiveDamaged == 0)) {
                    answer = 1;
                } else if ((countsIdx == counts.size() - 1 && consecutiveDamaged == counts.get(countsIdx))) {
                    answer = 1;
                }
                memo.put(this, answer);
                return answer;
            }

            if (row[rowIdx] == '?') {
                answer = handleDamaged() + handleWorking();
            } else if (row[rowIdx] == '#') {
                answer = handleDamaged();
            } else if (row[rowIdx] == '.') {
                answer = handleWorking();
            }

            memo.put(this, answer);
            return answer;
        }

        private long handleDamaged() {
            if (countsIdx == counts.size() || consecutiveDamaged >= counts.get(countsIdx)) {
                return 0;
            }
            return new RowOfSprings(row, counts, consecutiveDamaged + 1, rowIdx + 1, countsIdx)
                    .countPossibilities();
        }

        private long handleWorking() {
            if (consecutiveDamaged > 0) {
                if (counts.get(countsIdx) != consecutiveDamaged) {
                    return 0;
                }
                return new RowOfSprings(row, counts, 0, rowIdx + 1, countsIdx + 1)
                        .countPossibilities();
            }
            return new RowOfSprings(row, counts, 0, rowIdx + 1, countsIdx).countPossibilities();
        }

        // optimize hashmap methods for memoization
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            return (o instanceof RowOfSprings row) && (rowIdx == row.rowIdx) && (countsIdx == row.countsIdx)
                    && (consecutiveDamaged == row.consecutiveDamaged);
        }

        @Override
        public int hashCode() {
            return (PRIME * rowIdx) + (PRIME * countsIdx) + (PRIME * consecutiveDamaged);
        }
    }
}