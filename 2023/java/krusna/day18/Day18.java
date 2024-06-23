/*
 * DAY 18 NOTES:
 * - current strategy:
 *  - build a grid based on hte instructions
 *  - loop over grid and sum result
 * - problem:
 *  - grid too large
 *  - represent dug trench in a different way
 *  - essentials: width/height of array, traversing each row
 *  -
 *
 *
 */

package krusna.day18;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day18 {
    List<String> allLines = new ArrayList<>();
    List<Instruction> allInstructions = new ArrayList<>();

    enum Direction {
        EAST(0, 1), SOUTH(1, 0), WEST(0, -1), NORTH(-1, 0);

        final int rowOffset;
        final int colOffset;

        Direction (int rowOffset, int colOffset) {
            this.rowOffset = rowOffset;
            this.colOffset = colOffset;
        }

        public static Direction getDirection(Character c) {
            return switch(c) {
                case 'R', '0' -> EAST;
                case 'D', '1' -> SOUTH;
                case 'L', '2' -> WEST;
                case 'U', '3' -> NORTH;
                default -> NORTH;
            };
        }

        public char gridSymbol(Direction next) {
            if (this == EAST) {
                return switch(next) {
                    case EAST -> '-';
                    case SOUTH -> '7';
                    case WEST -> throw new IllegalStateException();
                    case NORTH -> 'J';
                };
            } else if (this == SOUTH) {
                return switch(next) {
                    case EAST -> 'L';
                    case SOUTH -> '|';
                    case WEST -> 'J';
                    case NORTH -> throw new IllegalStateException();
                };
            } else if (this == WEST) {
                return switch(next) {
                    case EAST -> throw new IllegalStateException();
                    case SOUTH -> 'F';
                    case WEST -> '-';
                    case NORTH -> 'L';
                };
            } else {
                return switch(next) {
                    case EAST -> 'F';
                    case SOUTH -> throw new IllegalStateException();
                    case WEST -> '7';
                    case NORTH -> '|';
                };
            }
        }
    }

    long hexToNumMeters(String hexCode) {
        return Long.parseLong(hexCode, 16);
    }

    void storeInstructions(int part) {
        if (part == 1) {
            for (String line : allLines) {
                String[] parts = line.split(" ");
                    allInstructions.add(
                        new Instruction(
                            Direction.getDirection(line.charAt(0)),
                            Integer.parseInt(parts[part])
                        )
                );
            }
        } else {
            for (String line : allLines) {
                String[] parts = line.split(" ");
                allInstructions.add(
                    new Instruction(
                        Direction.getDirection(parts[2].charAt(7)),
                        hexToNumMeters(parts[2].substring(2,7))
                    )
                );
            }
        }
    }

    long lagoonCapacity() {
        long minRows = 0, minCols = 0, maxRows = 0, maxCols = 0;
        long currRow = 0, currCol = 0;
        long res = 0;
        Direction prev = null;
        Direction start = null;
        Grid grid = new Grid();

        for (Instruction inst : allInstructions) {
            long dist = inst.dist;
            Direction dir = inst.dir;
            if (start == null) {
                start = dir;
            }
            if (prev != null) {
                grid.put(currCol, currRow, prev.gridSymbol(dir));
            }
            if (dir.rowOffset != 0) {
                for (int i = 0; i < dist; i++) {
                    currRow += dir.rowOffset;
                    grid.put(currCol, currRow, dir.gridSymbol(dir));
                    minRows = Math.min(minRows, currRow);
                    maxRows = Math.max(maxRows, currRow);
                }
            } else {
                currCol += (dir.colOffset * dist);
                minCols = Math.min(minCols, currCol);
                maxCols = Math.max(maxCols, currCol);
            }
            prev = dir;
        }
        grid.put(currCol, currRow, prev.gridSymbol(start));

        for (long row = minRows; row <= maxRows; row++) {
            boolean inTrench = false;
            boolean onBorder = false;
            boolean lastAdded = false;
            long prevColNum = minCols - 1;

            for (Map.Entry<Long, Character> colEntry : grid.gridMap.get(row).entrySet()) {
                if (inTrench || onBorder) {
                    res += colEntry.getKey() - prevColNum;
                    res += lastAdded ? 0 : 1;
                    lastAdded = true;
                } else {
                    lastAdded = false;
                }
                char c = colEntry.getValue();
                if (c == '|') {
                    inTrench = !inTrench;
                } else if (c == 'F' || c == '7') {
                    inTrench = !inTrench;
                    onBorder = !onBorder;
                } else {
                    onBorder = !onBorder;
                }
                prevColNum = colEntry.getKey();
            }
        }

        allInstructions.clear();
        return res;
    }

    record Instruction(Direction dir, long dist) {};

    static class Grid {
        HashMap<Long, TreeMap<Long, Character>> gridMap = new HashMap<>();

        public void put(long x, long y, char c) {
            if (!gridMap.containsKey(y)) {
                gridMap.put(y, new TreeMap<>());
            }
            gridMap.get(y).put(x, c);
        };
    }

    void part1() {
        storeInstructions(1);
        long res = lagoonCapacity();
        System.out.printf("Part 1: %d\n", res);
    }

    void part2() {
        storeInstructions(2);
        long res = lagoonCapacity();
        System.out.printf("Part 2: %d\n", res);
    }

    public static void main(String... args) throws Exception {
        Day18 day18 = new Day18();
        day18.allLines = Files.readAllLines(Paths.get("./2023/java/krusna/day18/day18.txt"));
        day18.part1();
        day18.part2();
    }
}
