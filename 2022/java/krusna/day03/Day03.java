import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day03 {
    public static void main(String... args) throws Exception {
        List<String> allLines = Files.readAllLines(Paths.get("../testInput/day3.txt"));
        int resultPart1 = 0, resultPart2 = 0;
        Set<Character> chars = new HashSet<>();
        List<String> threeRucksacks = new ArrayList<>(3);
        int counter = 0;

        for (String line : allLines) {
            int n = line.length();

            // part 1
            for (int i = 0; i < n / 2; i++)
                chars.add(line.charAt(i));
            checkCopy: for (int i = n / 2; i < n; i++) {
                if (chars.contains(line.charAt(i))) {
                    resultPart1 += scoreChar(line.charAt(i));
                    break checkCopy;
                }
            }
            chars.clear();

            if (counter == 2) {
                threeRucksacks.add(line);
                resultPart2 += scoreChar(findCommonChar(threeRucksacks));
                threeRucksacks.clear();
                counter = 0;
            } else {
                threeRucksacks.add(line);
                counter++;
            }
        }

        System.out.printf("part1: %d\n", resultPart1);
        System.out.printf("part2: %d\n", resultPart2);
    }

    private static int scoreChar(Character c) {
        if (Character.isUpperCase(c)) {
            return 26 + c - 'A' + 1;
        } else {
            return c - 'a' + 1;
        }
    }

    private static Character findCommonChar(List<String> s) {
        Map<Character, Integer> charFreq = new HashMap<>();
        for (int i = 0; i < 3; i++) {
            String str = s.get(i);
            Set<Character> uniqueChars = new HashSet<>();
            for (char c : str.toCharArray()) {
                uniqueChars.add(c);
            }
            for (char c : uniqueChars) {
                charFreq.put(c, charFreq.getOrDefault(c, 0) + 1);
                if (charFreq.get(c) == 3)
                    return c;
            }
        }
        return 'a';
    }
}
