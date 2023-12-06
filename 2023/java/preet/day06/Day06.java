package preet.day06;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day06 {

    private static class TimeToDistance {

        public Map<Long, Long> timeToDist = new HashMap<>();
        public long time;
        public long dist;
        public TimeToDistance(String text) {
            final String[] data = text.split("\n");
            for(int i = 0; i < data.length; i++) {
                data[i] = data[i].split(":")[1].trim();
            }
            final String[] times = data[0].split(" ");
            final String[] dists = data[1].split(" ");

            List<Long> timeVals = new ArrayList<>();
            StringBuilder sbTime = new StringBuilder();
            List<Long> distVals = new ArrayList<>();
            StringBuilder sbDist = new StringBuilder();

            for(String s: times) {
                s = s.trim();
                if (!s.isEmpty() && !s.isBlank()) {
                    sbTime.append(s);
                    timeVals.add(Long.parseLong(s));
                }
            }

            for(String s: dists) {
                s = s.trim();
                if (!s.isEmpty() && !s.isBlank()) {
                    sbDist.append(s);
                    distVals.add(Long.parseLong(s));
                }
            }

            time = Long.parseLong(sbTime.toString());
            dist = Long.parseLong(sbDist.toString());

            for(int i = 0; i < timeVals.size(); i++) {
                timeToDist.put(timeVals.get(i), distVals.get(i));
            }
        }
    }

    private final String filePath = "2023/java/preet/day06/input.txt";

    public static void main(String[] args) throws IOException {
        final Day06 day06 = new Day06();
        day06.partOne();
        day06.partTwo();
    }

    // t = time of race
    // d = max dist
    // k = amount of time button pressed
    // d = k(t - k) = tk - k^2
    // 0 = tk -k^2 - d
    // k^2 - tk + d = 0
    // k = (t +/- sqrt(t^2 - 4d)) / 2;

    public void partOne() throws IOException {
        final String text = Files.readString(Paths.get(filePath));
        final TimeToDistance timeToDistance = new TimeToDistance(text);
        long result = 1;
        for(Map.Entry<Long, Long> e: timeToDistance.timeToDist.entrySet()) {
            result *= waysToWin(e.getKey(), e.getValue());
        }
        System.out.println(result);
    }

    private long waysToWin(long time, long dist) {
        return maxTimeToWinTheRace(time, dist) - minTimeToWinTheRace(time, dist) + 1;
    }

    private long minTimeToWinTheRace(long time, long dist) {
        return (long)(Math.ceil((time - Math.sqrt(Math.pow(time,2) - (4 * dist))) / 2));
    }

    private long maxTimeToWinTheRace(long time, long dist) {
        return (long)(Math.floor((time + Math.sqrt(Math.pow(time,2) - (4 * dist))) / 2));
    }

    public void partTwo() throws IOException {
        final String text = Files.readString(Paths.get(filePath));
        final TimeToDistance timeToDistance = new TimeToDistance(text);
        System.out.println(waysToWin(timeToDistance.time, timeToDistance.dist));
    }
}
