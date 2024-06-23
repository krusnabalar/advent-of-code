package krusna.day17;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day17 {
    List<String> allLines = new ArrayList<>();
    HashSet<Node> visited = new HashSet<>();
    PriorityQueue<State> queue = new PriorityQueue<>();

    int[][] grid;
    int[] dest;

    void makeGrid() {
        grid = new int[allLines.size()][allLines.get(0).length()];
        dest = new int[] {allLines.size() - 1, allLines.get(0).length() - 1};

        for (int i = 0; i < allLines.size(); i++) {
            String line = allLines.get(i);
            for (int j = 0; j < line.length(); j++) {
                grid[i][j] = line.charAt(j) - '0';
            }
        }
    }

    enum Direction {
        NORTH(-1, 0), EAST(0, 1), SOUTH(1, 0), WEST(0, -1);

        final int rowOffset;
        final int colOffset;

        Direction (int rowOffset, int colOffset) {
            this.rowOffset = rowOffset;
            this.colOffset = colOffset;
        }

        public Direction turnLeft() {
            return switch (this) {
                case NORTH -> WEST;
                case WEST -> SOUTH;
                case SOUTH -> EAST;
                case EAST -> NORTH;
            };
        }

        public Direction turnRight() {
            return switch (this) {
                case NORTH -> EAST;
                case EAST -> SOUTH;
                case SOUTH -> WEST;
                case WEST -> NORTH;
            };
        }
    }

    record Node(int row, int col, Direction dir, int steps) {}

    static class State implements Comparable<State> {
        Node node;
        int cost;
        State prev;

        public State(Node node, int cost, State prev) {
            this.node = node;
            this.cost = cost;
            this.prev = prev;
        }

        @Override
        public int compareTo(State o) {return cost - o.cost;}

        private void addNextWhenInBounds(Collection<State> out, Direction d, int[][] costs) {
            int nr = node.row + d.rowOffset;
            int nc = node.col + d.colOffset;
            if (nr >= 0 && nr < costs.length && nc >= 0 && nc < costs[0].length) {
                int nSteps = node.dir == d ? node.steps + 1 : 1;
                out.add(new State(new Node(nr, nc, d, nSteps), cost + costs[nr][nc], this));
            }
        }

        public void addNext(Collection<State> out, int[][] costs, boolean ultra) {
            int maxSteps = ultra ? 10 : 3;
            if (node.steps < maxSteps) {
                addNextWhenInBounds(out, this.node.dir, costs);
            }
            if (!ultra || node.steps > 3) {
                addNextWhenInBounds(out, this.node.dir.turnLeft(), costs);
                addNextWhenInBounds(out, this.node.dir.turnRight(), costs);
            }
        }
    }

    int dijkstra(boolean ultra) {
        queue.add(new State(new Node(0, 1, Direction.EAST, 1), grid[0][1], null));
        queue.add(new State(new Node(1, 0, Direction.SOUTH, 1), grid[1][0], null));
        while (!queue.isEmpty()) {
            State cur = queue.remove();

            if (visited.contains(cur.node)) {
                continue;
            }
            visited.add(cur.node);

            if (cur.node.row == dest[0] && cur.node.col == dest[1] && (!ultra || cur.node.steps > 3)) {
                queue.clear();
                visited.clear();
                return cur.cost;
            }
            cur.addNext(queue, grid, ultra);
        }
        return -1;
    }

    void part1() {
        int res = dijkstra(false);
        System.out.printf("Part 1: %d\n", res);
    }

    void part2() {
        int res = dijkstra(true);
        System.out.printf("Part 2: %d\n", res);
    }

    public static void main(String... args) throws Exception {
        Day17 day17 = new Day17();
        day17.allLines = Files.readAllLines(Paths.get("./day17.txt"));

        day17.makeGrid();
        day17.part1();
        day17.part2();
    }
}
