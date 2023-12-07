package krusna.day05;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day05 {
    int numMaps = 7;
    List<Long> seedsPart1 = new ArrayList<>();
    Map<Long, Long> seedRanges = new HashMap<>();
    Map<Long, List<Long>>[] seedToLocation = new HashMap[numMaps];
    List<Long>[] startPoints = new ArrayList[numMaps];

    public static void main(String... args) throws Exception {
        List<String> allLines = Files.readAllLines(Paths.get("./day05.txt"));
        final Day05 day05 = new Day05();

        day05.fillMaps(allLines);
        day05.partOne();
        day05.partTwo();
    }

    private void fillMaps(List<String> allLines) {
        // instantiate maps and start lists
        for (int i = 0; i < numMaps; i++) {
            seedToLocation[i] = new HashMap<Long, List<Long>>();
            startPoints[i] = new ArrayList<>();
        }

        // fill seeds to start with
        String[] seedString = allLines.get(0).split(": ")[1].split(" ");
        for (String seed : seedString) {
            seedsPart1.add(Long.parseLong(seed));
        }
        for (int i = 0; i < seedString.length; i += 2) {
            seedRanges.put(Long.parseLong(seedString[i]), Long.parseLong(seedString[i + 1]));
        }
        // remove first line
        allLines.remove(0);

        // fill maps
        int mapIdx = -1;
        boolean skipLine = false;
        for (String line : allLines) {
            // if empty line, start next map and skip next line
            if (line.length() == 0) {
                mapIdx++;
                skipLine = true;
                continue;
            }
            // skip next line
            if (skipLine) {
                skipLine = false;
                continue;
            }
            // get destination start, source start, and range
            String[] mapIndices = line.split(" ");
            long destStart = Long.parseLong(mapIndices[0]);
            long sourceStart = Long.parseLong(mapIndices[1]);
            long rangeLength = Long.parseLong(mapIndices[2]);
            startPoints[mapIdx].add(sourceStart);
            seedToLocation[mapIdx].put(sourceStart, Arrays.asList(destStart, rangeLength));
        }
    }

    private long findDest(long source, int mapIdx) {
        // binary search through list to find map
        List<Long> mapStartPoints = startPoints[mapIdx];
        Collections.sort(mapStartPoints);
        if (source < mapStartPoints.get(0)) {
            return source;
        }
        int start = 0, end = mapStartPoints.size() - 1;
        int startPoint = 0;
        while (start < end) {
            int mid = start + (end - start) / 2;
            if (mapStartPoints.get(mid) <= source) {
                start = mid + 1;
                startPoint = (mapStartPoints.get(mid + 1) <= source) ? mid + 1 : mid;
            } else {
                end = mid - 1;
            }
        }

        long sourceStart = mapStartPoints.get(startPoint);
        // check if mapping exists
        if (source <= sourceStart + seedToLocation[mapIdx].get(sourceStart).get(1)) {
            return seedToLocation[mapIdx].get(sourceStart).get(0) + (source - sourceStart);
        }
        return source;
    }

    private long minimalLoc(List<Long> seeds) {
        long source = 0;
        long min = Long.MAX_VALUE;
        for (long seed : seeds) {
            source = seed;
            for (int mapIdx = 0; mapIdx < numMaps; mapIdx++) {
                source = findDest(source, mapIdx);
            }
            min = Math.min(min, source);
        }
        return min;
    }

    public void partOne() {
        long min = minimalLoc(seedsPart1);
        System.out.printf("Part 1: %d\n", min);
    }

    public void partTwo() {
        long min = Long.MAX_VALUE;
        for (Map.Entry<Long, Long> entry : seedRanges.entrySet()) {
            long seed = entry.getKey();
            long range = entry.getValue();
            for (long i = seed; i <= seed + range; i++) {
                long source = i;
                for (int mapIdx = 0; mapIdx < numMaps; mapIdx++) {
                    source = findDest(source, mapIdx);
                }
                min = Math.min(min, source);
            }
        }
        System.out.printf("Part 2: %d\n", min);
    }
}
