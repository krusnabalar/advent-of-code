package krusna.day15;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day15 {
    List<String> allLines;
    Map<Integer, List<Lens>> boxToLens = new HashMap<>();

    public class Lens {
        String label;
        int focalLength;

        private Lens(String label, int focalLength) {
            this.label = label;
            this.focalLength = focalLength;
        }

        private void setFocalLength(int focalLength) {
            this.focalLength = focalLength;
        }
    }

    int getHash(String seq, int part) {
        int currVal = 0;
        int n = 0;
        if (part == 1) {
            n = seq.length();
        } else {
            if (seq.charAt(seq.length() - 1) == '-') {
                n = seq.length() - 1;
            } else {
                n = seq.length() - 2;
            }
        }
        for (int i = 0; i < n; i++) {
            currVal = ((currVal + seq.charAt(i)) * 17) % 256;

        }
        return currVal;
    }

    void setHashMap() {
        initSeq: for (String seq : allLines) {
            int boxNum = getHash(seq, 2);
            if (seq.charAt(seq.length() - 1) == '-') {
                if (boxToLens.containsKey(boxNum)) {
                    List<Lens> updatedList = boxToLens.get(boxNum);
                    for (int i = 0; i < updatedList.size(); i++) {
                        if (updatedList.get(i).label.equals(seq.split("-")[0])) {
                            updatedList.remove(i);
                            continue initSeq;
                        }
                    }
                }
            } else {
                List<Lens> updatedList = boxToLens.getOrDefault(boxNum, new ArrayList<>());
                Lens lens = new Lens(seq.split("=")[0], Integer.parseInt(seq.split("=")[1]));
                for (int i = 0; i < updatedList.size(); i++) {
                    if (updatedList.get(i).label.equals(lens.label)) {
                        updatedList.get(i).setFocalLength(lens.focalLength);
                        boxToLens.put(boxNum, updatedList);
                        continue initSeq;
                    }
                }
                updatedList.add(lens);
                boxToLens.put(boxNum, updatedList);
            }
        }
    }

    int getFocusingPower() {
        int focusingPower = 0;
        for (int boxNum : boxToLens.keySet()) {
            int slotNum = 1;
            for (Lens l : boxToLens.get(boxNum)) {
                focusingPower += (boxNum + 1) * slotNum * l.focalLength;
                slotNum++;
            }
        }
        return focusingPower;
    }

    void part1() {
        int res = 0;
        for (String seq : allLines) {
            res += getHash(seq, 1);
        }
        System.out.printf("Part 1: %d\n", res);
    }

    void part2() {
        setHashMap();
        int res = getFocusingPower();
        System.out.printf("Part 2: %d\n", res);
    }

    public static void main(String... args) throws Exception {
        Day15 day15 = new Day15();
        day15.allLines = Files.readAllLines(Paths.get("./day15.txt"));
        day15.allLines = Arrays.stream(day15.allLines.get(0).split(",")).toList();

        day15.part1();
        day15.part2();
    }
}
