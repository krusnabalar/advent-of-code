package krusna.day23;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day23 {
    List<String> allLines;
    int sideLength;
    char[][] grid;
    Map<Integer, Coordinate> idxToVertex = new HashMap<>();
    Map<Coordinate, Integer> vertexToIdx = new HashMap<>();
    Map<Integer, List<Edge>> graph = new HashMap<>();
    Coordinate start, end;
    boolean partOne = true;

    void setupGrid() {
        sideLength = allLines.size();
        start = new Coordinate(0, 1);
        end = new Coordinate(sideLength - 1, sideLength - 2);
        grid = new char[sideLength][sideLength];
        for (int i = 0; i < sideLength; i++) {
            for (int j = 0; j < sideLength; j++) {
                grid[i][j] = allLines.get(i).charAt(j);
            }
        }
    }

    void buildGraph() {
        idxToVertex.clear();
        vertexToIdx.clear();
        graph.clear();
        findVertices();
        for (int idx : idxToVertex.keySet()) {
            explorePathsFromVertex(idx);
        }
    }

    int dfs(int currIdx, int dist, Set<Integer> visited) {
        if (idxToVertex.get(currIdx).equals(end)) {
            return dist;
        }
        int maxDistance = 0;
        Set<Integer> newVisited = new HashSet<>(visited);
        newVisited.add(currIdx);
        for (Edge edge : graph.getOrDefault(currIdx, new ArrayList<>())) {
            if (!visited.contains(vertexToIdx.get(edge.end))) {
                int result = dfs(vertexToIdx.get(edge.end), dist + edge.distance, newVisited);
                maxDistance = Math.max(maxDistance, result);
            }
        }

        return maxDistance;
    }

    void findVertices() {
        int vertexId = 0;
        idxToVertex.put(vertexId, start);
        vertexToIdx.put(start, vertexId);
        vertexId++;
        for (int i = 0; i < sideLength; i++) {
            for (int j = 0; j < sideLength; j++) {
                if (isValidChar(grid[i][j]) && isVertex(new Coordinate(i, j))) {
                        idxToVertex.put(vertexId, new Coordinate(i, j));
                        vertexToIdx.put(new Coordinate(i, j), vertexId);
                        vertexId++;
                }
            }
        }
        idxToVertex.put(vertexId, end);
        vertexToIdx.put(end, vertexId);
    }

    boolean isValidChar(char c) {
        return c == '.' || c == 'v' || c == '<' || c == '>' || c == '^';
    }

    boolean isValidCoordinate(Coordinate c) {
        return c.x >= 0 && c.x < sideLength && c.y >= 0 && c.y < sideLength;
    }

    boolean invalidDirection(char c, Direction oppositeDir) {
        return (c == '<' && oppositeDir == Direction.WEST) || (c == '>' && oppositeDir == Direction.EAST) ||
        (c == '^' && oppositeDir == Direction.NORTH) || (c == 'v' && oppositeDir == Direction.SOUTH);
    }

    boolean isVertex(Coordinate gridElement) {
        int i = gridElement.x;
        int j = gridElement.y;
        if (i == 0 || i == sideLength - 1 || j == 0 || j == sideLength - 1) {
            return false;
        }
        int numValidNeighbors = 0;
        for (Direction dir : Direction.values()) {
            if (isValidChar(grid[i + dir.rowOffset][j + dir.colOffset])) {
                numValidNeighbors++;
            }
        }
        return numValidNeighbors > 2;
    }

    void explorePathsFromVertex(int vertexIdx) {
        Coordinate vertex = idxToVertex.get(vertexIdx);
        for (Direction dir : Direction.values()) {
            Coordinate next = new Coordinate(vertex.x + dir.rowOffset, vertex.y + dir.colOffset);
            if (isValidCoordinate(next) && isValidChar(grid[next.x][next.y])) {
                int[] neighborDistance = getNextVertex(next, dir.opposite(), 1);
                if (neighborDistance == null) {
                    continue;
                }
                List<Edge> edges = graph.getOrDefault(vertexIdx, new ArrayList<>());
                edges.add(new Edge(vertex, idxToVertex.get(neighborDistance[0]), neighborDistance[1]));
                graph.put(vertexIdx, edges);
            }
        }
    }

    public int[] getNextVertex(Coordinate next, Direction oppositeDir, int distance) {
        if (idxToVertex.containsValue(next)) {
            return new int[] {vertexToIdx.get(next), distance};
        }
        char c = grid[next.x][next.y];
        if (partOne && invalidDirection(c, oppositeDir)) {
            return null;
        }
        for (Direction dir : Direction.values()) {
            if (dir != oppositeDir && isValidChar(grid[next.x + dir.rowOffset][next.y + dir.colOffset])) {
                return getNextVertex(new Coordinate(next.x + dir.rowOffset, next.y + dir.colOffset), dir.opposite(), distance + 1);
            }
        }
        return null;
    }

    public static void main(String... args) throws Exception {
        Day23 day23 = new Day23();
        day23.allLines = Files.readAllLines(Paths.get("./2023/java/krusna/day23/day23.txt"));
        day23.setupGrid();
        day23.buildGraph();

        System.out.printf("Part 1: %d\n", day23.dfs(0, 0, new HashSet<>()));
        day23.partOne = false;
        day23.buildGraph();
        System.out.printf("Part 2: %d\n", day23.dfs(0, 0, new HashSet<>()));
    }
}

class Coordinate {
    int x;
    int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate pos = (Coordinate) o;
        return x == pos.x && y == pos.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

class Edge {
    Coordinate start;
    Coordinate end;
    int distance;

    public Edge(Coordinate start, Coordinate end, int distance) {
        this.start = start;
        this.end = end;
        this.distance = distance;
    }
}

enum Direction {
    EAST(0, 1), SOUTH(1, 0), WEST(0, -1), NORTH(-1, 0);

    final int rowOffset;
    final int colOffset;

    Direction (int rowOffset, int colOffset) {
        this.rowOffset = rowOffset;
        this.colOffset = colOffset;
    }

    Direction opposite() {
        switch (this) {
            case EAST:
                return WEST;
            case WEST:
                return EAST;
            case NORTH:
                return SOUTH;
            case SOUTH:
                return NORTH;
            default:
                return null;
        }
    }
}
