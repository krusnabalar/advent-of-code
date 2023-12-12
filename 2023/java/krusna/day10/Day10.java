package krusna.day10;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day10 {
    Map<Integer, Set<Character>> validPipeMap = Map.of(
            0, new HashSet<>(Arrays.asList('|', 'F', '7')),
            1, new HashSet<>(Arrays.asList('-', 'J', '7')),
            2, new HashSet<>(Arrays.asList('|', 'J', 'L')),
            3, new HashSet<>(Arrays.asList('-', 'F', 'L')));
    Map<Character, Set<Integer>> validDirMap = Map.of(
            '.', new HashSet<>(),
            '7', new HashSet<>(Arrays.asList(2, 3)),
            'F', new HashSet<>(Arrays.asList(1, 2)),
            'L', new HashSet<>(Arrays.asList(0, 1)),
            'J', new HashSet<>(Arrays.asList(0, 3)),
            '|', new HashSet<>(Arrays.asList(0, 2)),
            '-', new HashSet<>(Arrays.asList(1, 3)),
            'S', new HashSet<>(Arrays.asList(0, 1, 2, 3)));
    List<String> allLines;
    int rows;
    int cols;
    int[] startingPos;
    char[][] pipeMatrix;
    int[][] visitedPipes;
    int count = 0;

    int directionInX[] = new int[] { -1, 0, 1, 0 };
    int directionInY[] = new int[] { 0, 1, 0, -1 };

    public void fillPipes() {
        rows = allLines.size();
        cols = allLines.get(0).length();

        pipeMatrix = new char[rows][cols];
        visitedPipes = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            char[] line = allLines.get(i).toCharArray();
            for (int j = 0; j < cols; j++) {
                pipeMatrix[i][j] = line[j];
                visitedPipes[i][j] = -1;
                if (line[j] == 'S') {
                    startingPos = new int[] { i, j };
                    visitedPipes[i][j] = 0;
                }
            }
        }

        // figure out S, update to correct pipe
        Set<Integer> validDirs = new HashSet<>();
        for (int i = 0; i < 4; i++) {
            int newX = startingPos[0] + directionInX[i];
            int newY = startingPos[1] + directionInY[i];
            if (newX >= rows || newX < 0 || newY >= cols || newY < 0)
                continue;
            if (validPipeMap.get(i).contains(pipeMatrix[newX][newY]))
                validDirs.add(i);
        }
        for (char c : validDirMap.keySet()) {
            if (validDirs.equals(validDirMap.get(c))) {
                pipeMatrix[startingPos[0]][startingPos[1]] = c;
            }
        }
    }

    public int traverseLoop(int[] pipe, int dirFrom) {
        int loopSize = 1;
        char currPipe = pipeMatrix[pipe[0]][pipe[1]];
        int nextDir = 0;

        while (!Arrays.equals(pipe, startingPos)) {
            for (int d : validDirMap.get(currPipe)) {
                if (dirFrom != d) {
                    nextDir = d;
                }
            }
            dirFrom = (nextDir + 2) % 4;
            pipe[0] += directionInX[nextDir];
            pipe[1] += directionInY[nextDir];
            currPipe = pipeMatrix[pipe[0]][pipe[1]];
            loopSize++;
        }

        return loopSize;
    }

    public void part1() {
        int dir = validDirMap.get(pipeMatrix[startingPos[0]][startingPos[1]]).iterator().next();
        int[] nextPos = new int[] { startingPos[0] + directionInX[dir], startingPos[1] + directionInY[dir] };
        int loopSize = traverseLoop(nextPos, (dir + 2) % 4);
        System.out.printf("Part 1: %d\n", loopSize / 2);
    }

    public static void main(String... args) throws Exception {
        Day10 day10 = new Day10();
        day10.allLines = Files.readAllLines(Paths.get("./day10.txt"));
        day10.fillPipes();
        day10.part1();
    }
}
