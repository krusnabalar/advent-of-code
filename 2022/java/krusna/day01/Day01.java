import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.nio.file.Files;

class Day01 {
    public static void main(String... args) throws Exception {
        Map<Integer, Integer> tally = fillHashMap();

        int maxSum = 0;
        int k = 3;
        int[] topK = new int[k];
        int minTopK = 0;
        int minIdx = 0;
        for (int currSum : tally.values()) {
            if (currSum > maxSum)
                maxSum = currSum;
            if (currSum > minTopK) {
                topK[minIdx] = currSum;
                minTopK = Integer.MAX_VALUE;
                for (int i = 0; i < k; i++) {
                    if (topK[i] < minTopK) {
                        minIdx = i;
                        minTopK = topK[i];
                    }
                }
            }
        }

        int topKSum = 0;
        for (int cal : topK)
            topKSum += cal;

        System.out.printf("part 1: %d\n", maxSum);
        System.out.printf("part 2: %d\n", topKSum);
    }

    private static Map<Integer, Integer> fillHashMap() throws Exception {
        Map<Integer, Integer> tally = new HashMap<>();
        int[] elfNum = new int[] { 0 };
        Files.readAllLines(Path.of("../input/day1.txt")).forEach(line -> {
            if (line.isEmpty()) {
                elfNum[0]++;
            } else {
                tally.put(elfNum[0], tally.getOrDefault(elfNum[0], 0) + Integer.parseInt(line));
            }
        });
        return tally;
    }
}
