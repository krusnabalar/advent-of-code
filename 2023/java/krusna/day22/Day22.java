package krusna.day22;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day22 {
    List<String> allLines;
    Map<Integer, Brick> idxToBrick = new HashMap<>();
    List<Brick> bricks = new ArrayList<>();
    int[][] maxHeightMap;
    int[][][] space;
    int maxX, maxY, maxZ;

    void parseAndSortInput() {
        for (int i = 0; i < allLines.size(); i++) {
            String[] parts = allLines.get(i).split("~");
            Coordinate start = Coordinate.getCoordFromInput(parts[0].split(","));
            Coordinate end = Coordinate.getCoordFromInput(parts[1].split(","));
            Brick b = new Brick(i, start, end);
            idxToBrick.put(i, b);
            bricks.add(b);
        }

        maxX = bricks.stream().mapToInt(b -> Math.max(b.start.x, b.end.x)).max().orElse(0) + 1;
        maxY = bricks.stream().mapToInt(b -> Math.max(b.start.y, b.end.y)).max().orElse(0) + 1;
        maxZ = bricks.stream().mapToInt(b -> b.end.z).max().orElse(0) + 1;
        space = new int[maxX][maxY][maxZ];

        maxHeightMap = new int[maxX][maxY];
        bricks.sort(Comparator.comparingInt(b -> b.start.z));
    }

    void simulateFall() {
        for (Brick brick : bricks) {
            int maxHeight = getMaxHeight(brick);
            brick.fall(maxHeight);
            updateHeightMap(brick);
        }
    }

    int getMaxHeight(Brick brick) {
        int maxHeight = 0;
        for (int x = brick.start.x; x <= brick.end.x; x++) {
            for (int y = brick.start.y; y <= brick.end.y; y++) {
                maxHeight = Math.max(maxHeight, maxHeightMap[x][y]);
            }
        }
        return maxHeight;
    }

    void updateHeightMap(Brick brick) {
        for (int x = brick.start.x; x <= brick.end.x; x++) {
            for (int y = brick.start.y; y <= brick.end.y; y++) {
                maxHeightMap[x][y] = brick.end.z;
                for (int z = brick.start.z; z <= brick.end.z; z++) {
                    space[x][y][z] = brick.idx + 1;
                }
            }
        }
    }

    void setupSupports() {
        for (Brick brick : bricks) {
            Set<Integer> supporters = new HashSet<>();
            for (int x = brick.start.x; x <= brick.end.x; x++) {
                for (int y = brick.start.y; y <= brick.end.y; y++) {
                    int z = brick.start.z - 1;
                    if (z > 0 && space[x][y][z] != 0 && space[x][y][z] != brick.idx + 1) {
                        supporters.add(space[x][y][z] - 1);
                    }
                }
            }
            for (int idx : supporters) {
                Brick supportingBrick = idxToBrick.get(idx);
                brick.supportedBy.add(supportingBrick);
                supportingBrick.supporting.add(brick);
            }
        }
    }

    int sumOfFallingBricks() {
        int sum = 0;
        for (Brick brick : bricks) {
            Set<Brick> falling = new HashSet<>();
            falling.add(brick);
            Queue<Brick> queue = new LinkedList<>(brick.supporting);

            while (!queue.isEmpty()) {
                Brick current = queue.poll();
                if (current.supportedBy.stream().allMatch(falling::contains)) {
                    if (falling.add(current)) {
                        queue.addAll(current.supporting);
                    }
                }
            }
            sum += falling.size() - 1;
        }
        return sum;
    }

    void part1() {
        System.out.printf(
            "Part 1: %d\n",
            (int) bricks.stream()
                .filter(brick -> brick.supporting.stream().allMatch(b -> b.supportedBy.size() > 1))
                .count());
    }

    void part2() {
        System.out.printf("Part 2: %d\n", sumOfFallingBricks());
    }

    public static void main(String[] args) throws Exception {
        Day22 day22 = new Day22();
        day22.allLines = Files.readAllLines(Paths.get("./day22.txt"));
        day22.parseAndSortInput();
        day22.simulateFall();
        day22.setupSupports();

        day22.part1();
        day22.part2();
    }
}

class Brick {
    int idx;
    Coordinate start, end;
    Set<Brick> supporting = new HashSet<>();
    Set<Brick> supportedBy = new HashSet<>();

    public Brick(int idx, Coordinate start, Coordinate end) {
        this.idx = idx;
        this.start = start;
        this.end = end;
    }

    public void fall(int height) {
        int brickHeight = end.z - start.z;
        start.z = height + 1;
        end.z = brickHeight + start.z;
    }
}

class Coordinate {
    int x, y, z;

    public Coordinate(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Coordinate getCoordFromInput(String[] input) {
        return new Coordinate(
            Integer.parseInt(input[0]),
            Integer.parseInt(input[1]),
            Integer.parseInt(input[2]));
    }
}
