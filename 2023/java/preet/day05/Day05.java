package preet.day05;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

public class Day05 {
    private final String filePath = "2023/java/preet/day05/input.txt";

    private static class RangePair {
        public long destStart;
        public long range;
        public RangePair(long destStart, long range) {
            this.destStart = destStart;
            this.range = range;
        }
    }

    public static void main(String[] args) throws IOException {
        final Day05 day05 = new Day05();
        day05.partOne();
        day05.partTwo();
    }

    public void partOne() throws IOException {
        String text = Files.readString(Paths.get(filePath));
        List<TreeMap<Long, RangePair>> matrix = getMatrix(text);

        String[] seedListVals = text.split("\n\n")[0].split(":")[1].trim().split(" ");
        List<Long> seedList = Arrays.stream(seedListVals).map(Long::parseLong).toList();
        long min = seedList.stream().map((e) -> search(matrix, e, 0)).toList().stream().min(Long::compare).get();
        System.out.println(min);
    }

    private long search(List<TreeMap<Long, RangePair>> matrix, long currItemVal, int level) {
        final TreeMap<Long, RangePair> map = matrix.get(level);
        Long val = map.floorKey(currItemVal);
        long mapping = currItemVal;
        if (val != null &&  currItemVal <= val + map.get(val).range - 1) {
            long diff = currItemVal - val;
            mapping = map.get(val).destStart + diff;
        }

        if (level == matrix.size() - 1) {
            return mapping;
        }

        return search(matrix, mapping, level+1);
    }

    private List<TreeMap<Long, RangePair>> getMatrix(final String mapping) {
        final List<TreeMap<Long, RangePair>> result = new ArrayList<>();
        String[] paragraphs = mapping.split("\n\n");
        for(int i = 1; i <= 7; i++) {
            result.add(getTreeMap(paragraphs[i]));
        }
        return result;
    }

    private TreeMap<Long, RangePair> getTreeMap(final String info) {
        final String[] values = info.split("\n");
        final TreeMap<Long, RangePair> treeMap = new TreeMap<>();
        for(int i = 1; i < values.length; i++) {
            String[] vals = values[i].trim().split(" ");
            List<Long> valList = Arrays.stream(vals).map(Long::parseLong).toList();
            RangePair rp = new RangePair(valList.get(0), valList.get(2));
            treeMap.put(valList.get(1), rp);
        }
        return treeMap;
    }



    public void partTwo() throws IOException {
        String text = Files.readString(Paths.get(filePath));
        List<TreeMap<Long, RangePair>> matrix = getMatrix(text);

        String[] seedListVals = text.split("\n\n")[0].split(":")[1].trim().split(" ");
        List<Long> seedList = Arrays.stream(seedListVals).map(Long::parseLong).toList();
        long min = Long.MAX_VALUE;
        for(int i = 0; i < seedList.size(); i+=2) {
            for(int j = 0; j < seedList.get(i+1); j++) {
                min = Math.min(min, search(matrix, seedList.get(i)+j, 0));
            }
        }
        System.out.println(min);
    }
}
