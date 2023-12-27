package krusna.day16;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day16 {
    List<String> allLines = new ArrayList<>();
    char[][] grid;
    // WSEN
    int directionInX[] = new int[] { -1, 0, 1, 0 };
    int directionInY[] = new int[] { 0, 1, 0, -1 };

    public class Incoming {
        int[] tile;
        int direction;

        private Incoming(int[] tile, int direction) {
            this.tile = tile;
            this.direction = direction;
        }
    }

    void setGrid() {
        int rowNum = 0;
        grid = new char[allLines.size()][];
        for (String line : allLines) {
            grid[rowNum++] = line.toCharArray();
        }
    }

    int countVisited(int[][] visited) {
        int res = 0;
        for (int i = 0; i < visited.length; i++) {
            for (int j = 0; j < visited.length; j++) {
                res += visited[i][j] > 0 ? 1 : 0;
            }
        }
        return res;
    }

    boolean isValidTile(int[] tile) {
        return !(tile[0] < 0 || tile[0] >= grid.length || tile[1] < 0 || tile[1] >= grid[0].length);
    }

    List<Incoming> getOutgoing(Incoming incoming) {
        List<Incoming> outgoing = new ArrayList<>();
        int[] pos = incoming.tile;
        int dir = incoming.direction;
        char tileType = grid[pos[0]][pos[1]];
        int[] nextTile, otherNextTile;
        int nextDir = 0;
        switch (tileType) {
            case '.':
                nextTile = new int[] { pos[0] + directionInY[dir], pos[1] + directionInX[dir] };
                if (isValidTile(nextTile)) {
                    outgoing.add(new Incoming(nextTile, dir));
                }
                break;
            case '-':
                if (dir == 0 || dir == 2) {
                    nextTile = new int[] { pos[0] + directionInY[dir], pos[1] + directionInX[dir] };
                    if (isValidTile(nextTile)) {
                        outgoing.add(new Incoming(nextTile, dir));
                    }
                } else {
                    int dir1 = 0, dir2 = 2;
                    nextTile = new int[] { pos[0] + directionInY[dir1], pos[1] + directionInX[dir1] };
                    otherNextTile = new int[] { pos[0] + directionInY[dir2], pos[1] + directionInX[dir2] };
                    if (isValidTile(nextTile))
                        outgoing.add(new Incoming(nextTile, dir1));
                    if (isValidTile(otherNextTile)) {
                        outgoing.add(new Incoming(otherNextTile, dir2));
                    }
                }
                break;
            case '|':
                if (dir == 1 || dir == 3) {
                    nextTile = new int[] { pos[0] + directionInY[dir], pos[1] + directionInX[dir] };
                    if (isValidTile(nextTile)) {
                        outgoing.add(new Incoming(nextTile, dir));
                    }
                } else {
                    int dir1 = 1, dir2 = 3;
                    nextTile = new int[] { pos[0] + directionInY[dir1], pos[1] + directionInX[dir1] };
                    otherNextTile = new int[] { pos[0] + directionInY[dir2], pos[1] + directionInX[dir2] };
                    if (isValidTile(nextTile))
                        outgoing.add(new Incoming(nextTile, dir1));
                    if (isValidTile(otherNextTile))
                        outgoing.add(new Incoming(otherNextTile, dir2));
                }
                break;
            case '/':
                if (dir == 0) {
                    nextDir = 1;
                } else if (dir == 1) {
                    nextDir = 0;
                } else if (dir == 2) {
                    nextDir = 3;
                } else {
                    nextDir = 2;
                }
                nextTile = new int[] { pos[0] + directionInY[nextDir], pos[1] + directionInX[nextDir] };
                if (isValidTile(nextTile)) {
                    outgoing.add(new Incoming(nextTile, nextDir));
                }
                break;
            case '\\':
                if (dir == 0) {
                    nextDir = 3;
                } else if (dir == 1) {
                    nextDir = 2;
                } else if (dir == 2) {
                    nextDir = 1;
                } else {
                    nextDir = 0;
                }
                nextTile = new int[] { pos[0] + directionInY[nextDir], pos[1] + directionInX[nextDir] };
                if (isValidTile(nextTile)) {
                    outgoing.add(new Incoming(nextTile, nextDir));
                }
                break;
            default:
        }
        return outgoing;
    }

    int traverseGrid(Incoming start) {
        int[][] visited = new int[allLines.size()][allLines.get(0).length()];
        Map<List<Integer>, Set<Integer>> checked = new HashMap<>();
        Queue<Incoming> queue = new LinkedList<>();
        queue.add(start);
        while (!queue.isEmpty()) {
            Incoming currTile = queue.poll();
            if (checked.containsKey(new ArrayList<>(Arrays.asList(currTile.tile[0], currTile.tile[1])))) {
                if (checked.get(new ArrayList<>(Arrays.asList(currTile.tile[0], currTile.tile[1])))
                        .contains(currTile.direction)) {
                    continue;
                }
            }
            visited[currTile.tile[0]][currTile.tile[1]] = 1;
            List<Incoming> outgoing = getOutgoing(currTile);
            Set<Integer> setOfDirs = checked.getOrDefault(currTile.tile, new HashSet<>());
            setOfDirs.add(currTile.direction);
            checked.put(new ArrayList<>(Arrays.asList(currTile.tile[0], currTile.tile[1])), setOfDirs);
            for (Incoming o : outgoing) {
                queue.add(o);
            }
        }

        return countVisited(visited);
    }

    int getMaxVisited() {
        int maxRes = -1;
        // top left corner
        maxRes = Math.max(traverseGrid(new Incoming(new int[] { 0, 0 }, 1)), maxRes);
        maxRes = Math.max(traverseGrid(new Incoming(new int[] { 0, 0 }, 2)), maxRes);
        // bottom left corner
        maxRes = Math.max(traverseGrid(new Incoming(new int[] { 0, grid.length - 1 }, 2)), maxRes);
        maxRes = Math.max(traverseGrid(new Incoming(new int[] { 0, grid.length - 1 }, 3)), maxRes);
        // top right corner
        maxRes = Math.max(traverseGrid(new Incoming(new int[] { grid[0].length - 1, 0 }, 0)), maxRes);
        maxRes = Math.max(traverseGrid(new Incoming(new int[] { grid[0].length - 1, 0 }, 1)), maxRes);
        // bottom right corner
        maxRes = Math.max(traverseGrid(new Incoming(new int[] { grid[0].length - 1, grid.length - 1 }, 0)), maxRes);
        maxRes = Math.max(traverseGrid(new Incoming(new int[] { grid[0].length - 1, grid.length - 1 }, 3)), maxRes);

        for (int i = 1; i < grid.length - 1; i++) {
            maxRes = Math.max(traverseGrid(new Incoming(new int[] { i, 0 }, 2)), maxRes);
            maxRes = Math.max(traverseGrid(new Incoming(new int[] { i, grid.length - 1 }, 0)), maxRes);
        }

        for (int i = 1; i < grid[0].length - 1; i++) {
            maxRes = Math.max(traverseGrid(new Incoming(new int[] { 0, i }, 1)), maxRes);
            maxRes = Math.max(traverseGrid(new Incoming(new int[] { grid[0].length - 1, i }, 3)), maxRes);
        }

        return maxRes;
    }

    void part1() {
        int res = traverseGrid(new Incoming(new int[] { 0, 0 }, 2));
        System.out.printf("Part 1: %d\n", res);
    }

    void part2() {
        int maxRes = getMaxVisited();
        System.out.printf("Part 2: %d\n", maxRes);
    }

    public static void main(String... args) throws Exception {
        Day16 day16 = new Day16();
        day16.allLines = Files.readAllLines(Paths.get("./day16.txt"));
        day16.setGrid();
        day16.part1();
        day16.part2();
    }
}
