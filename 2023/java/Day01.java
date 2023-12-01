/*


*/

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day01 {
    private static int twoPointerPart1(String s, int len) {
        int resL = -1, resR = -1;
        for (int i = 0; i < len; i++) {
            if (Character.isDigit(s.charAt(i))) {
                if (resL == -1)
                    resL = Character.getNumericValue(s.charAt(i));
                resR = Character.getNumericValue(s.charAt(i));
            }
        }
        if (resL != -1 && resR != -1)
            return resL * 10 + resR;
        return -1;
    }

    public static void main(String... args) throws Exception {
        List<String> allLines = Files.readAllLines(Paths.get("../input/day01.txt"));
        int totalCalibrationValuePart1 = 0, totalCalibrationValuePart2 = 0;
        Map<String, String> strToDigits = Map.of("one", "o1e", "two", "t2o", "three", "t3e", "four", "f4r", "five",
                "f5e", "six", "s6x",
                "seven", "s7n", "eight", "e8t", "nine", "n9e");
        for (String s : allLines) {
            totalCalibrationValuePart1 += twoPointerPart1(s, s.length());
            for (String key : strToDigits.keySet()) {
                s = s.replaceAll(key, strToDigits.get(key));
            }
            totalCalibrationValuePart2 += twoPointerPart1(s, s.length());
        }

        System.out.printf("part 1: %d\n", totalCalibrationValuePart1);
        System.out.printf("part 2: %d\n", totalCalibrationValuePart2);
    }
}