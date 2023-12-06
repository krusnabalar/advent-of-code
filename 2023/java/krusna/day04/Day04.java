package krusna.day04;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day04 {
    public static void main(String... args) throws Exception {
        List<String> allLines = Files.readAllLines(Paths.get("./day04.txt"));
        Set<Integer> winningSet = new HashSet<>();
        Map<Integer, Integer> numCopies = new HashMap<>();
        int p1 = 0;
        int p2 = 0;

        for (String line : allLines) {
            String[] splitLine = line.split(": ");
            int cardNum = Integer.parseInt(splitLine[0].split("Card")[1].trim());
            String nums = splitLine[1].replaceAll("  ", " ").trim();
            String[] winningNums = nums.split(" \\| ")[0].replaceAll("  ", " ").split(" ");
            String[] lotteryNums = nums.split(" \\| ")[1].replaceAll("  ", " ").split(" ");

            // fill winning set with all winning numbers
            for (String num : winningNums) {
                winningSet.add(Integer.parseInt(num));
            }

            // add points for lottery numbers that are in the winning set
            int numMatches = 0;
            for (String num : lotteryNums) {
                String numMod = num.replaceAll(" ", "");
                if (winningSet.remove(Integer.parseInt(numMod))) {
                    numMatches++;
                }
            }

            int currCopies = numCopies.getOrDefault(cardNum, 1);
            for (int i = cardNum + 1; i < cardNum + 1 + numMatches; i++) {
                numCopies.put(i, numCopies.getOrDefault(i, 1) + currCopies);
            }

            p1 += numMatches == 0 ? 0 : Math.pow(2, numMatches - 1);
            p2 += currCopies;
            winningSet.clear();
            numCopies.remove(cardNum);
        }

        System.out.printf("Part 1: %d\n", p1);
        System.out.printf("Part 1: %d\n", p2);
    }
}
