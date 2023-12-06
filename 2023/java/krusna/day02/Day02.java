package krusna.day02;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day02 {
    public static void main(String... args) throws Exception {
        List<String> allLines = Files.readAllLines(Paths.get("./day02.txt"));
        Map<String, Integer> colorToCount = Map.of("red", 12, "green", 13, "blue", 14);
        int gameIDCount = 1;
        int part1 = 0, part2 = 0;
        boolean isValidGame = true;
        for (String s : allLines) {
            String[] cubesInHand = s.split(": ")[1].split("; ");
            int minRed = -1, minGreen = -1, minBlue = -1;
            for (String singleHandful : cubesInHand) {
                String[] countPerColor = singleHandful.split(", ");
                for (String colorCount : countPerColor) {
                    String color = colorCount.split(" ")[1];
                    int count = Integer.parseInt(colorCount.split(" ")[0]);
                    if (colorToCount.get(color) < count)
                        isValidGame = false;
                    if (color.equals("red")) {
                        minRed = (minRed < 0) ? count : Math.max(minRed, count);
                    } else if (color.equals("blue")) {
                        minGreen = (minGreen < 0) ? count : Math.max(minGreen, count);
                    } else {
                        minBlue = (minBlue < 0) ? count : Math.max(minBlue, count);
                    }
                }
            }
            part1 += isValidGame ? gameIDCount : 0;
            part2 += Math.max(minRed, 0) * Math.max(minGreen, 0) * Math.max(minBlue, 0);
            isValidGame = true;
            gameIDCount++;
        }

        System.out.printf("Part 1: %d\n", part1);
        System.out.printf("Part 1: %d\n", part2);
    }
}
