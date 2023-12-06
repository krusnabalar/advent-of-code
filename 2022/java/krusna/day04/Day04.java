import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day04 {
    public static void main(String... args) throws Exception {
        List<String> allLines = Files.readAllLines(Paths.get("../input/day4.txt"));
        int fullyOverlapping = 0, anyOverlapping = 0;

        for (String line : allLines) {
            String[] ranges = line.split(",");

            String[] elf1 = ranges[0].split("-");
            int elf1Min = Integer.parseInt(elf1[0]);
            int elf1Max = Integer.parseInt(elf1[1]);

            String[] elf2 = ranges[1].split("-");
            int elf2Min = Integer.parseInt(elf2[0]);
            int elf2Max = Integer.parseInt(elf2[1]);

            if (isOverlapping(elf1Min, elf2Min, elf1Max, elf2Max)) {
                if (isFullyOverlapping(elf1Min, elf2Min, elf1Max, elf2Max)) {
                    fullyOverlapping++;
                }
                anyOverlapping++;
            }
        }

        System.out.printf("part 1: %d\n", fullyOverlapping);
        System.out.printf("part 2: %d\n", anyOverlapping);

    }

    private static boolean isOverlapping(int elf1Min, int elf2Min, int elf1Max, int elf2Max) {
        return Math.min(elf1Max, elf2Max) - Math.max(elf1Min, elf2Min) >= 0;
    }

    private static boolean isFullyOverlapping(int elf1Min, int elf2Min, int elf1Max, int elf2Max) {
        return ((elf1Min <= elf2Min && elf1Max >= elf2Max) || (elf2Min <= elf1Min && elf2Max >= elf1Max));
    }
}
