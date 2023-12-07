package krusna.day06;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day06 {
    String[] times;
    String[] distances;
    int numRaces;

    public static void main(String... args) throws Exception {
        List<String> allLines = Files.readAllLines(Paths.get("./day06.txt"));
        final Day06 day06 = new Day06();

        day06.processStrings(allLines);
        day06.partOne();
        day06.partTwo();
    }

    private void processStrings(List<String> allLines) {
        times = allLines.get(0).split(":")[1].trim().split(" +");
        distances = allLines.get(1).split(":")[1].trim().split(" +");
        numRaces = times.length;
    }

    private long numWaysToWin(long time, long dist) {
        for (long i = 0; i <= time / 2; i++) {
            if (i * (time - i) > dist)
                return (time + 1 - (i * 2));
        }

        return 0;
    }

    public void partOne() {
        long p1 = 1;

        for (int i = 0; i < numRaces; i++) {
            long time = Integer.parseInt(times[i]);
            long distance = Integer.parseInt(distances[i]);
            p1 *= numWaysToWin(time, distance);
        }

        System.out.printf("Part 1: %d\n", p1);
    }

    public void partTwo() {
        long p2 = 0;
        String timeStr = "";
        String distanceStr = "";
        for (int i = 0; i < numRaces; i++) {
            timeStr += times[i];
            distanceStr += distances[i];
        }

        long time = Long.parseLong(timeStr);
        long distance = Long.parseLong(distanceStr);
        p2 = numWaysToWin(time, distance);

        System.out.printf("Part 2: %d\n", p2);
    }
}
