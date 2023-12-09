package krusna.day09;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day09 {
    List<long[]> histories = new ArrayList<>();
    List<List<Long>> seqLists = new ArrayList<>();
    List<String> allLines;

    public void SetHistory() {
        for (String line : allLines) {
            String[] recordedValues = line.trim().replaceAll("/  +/g", " ").split(" ");
            long[] history = new long[recordedValues.length];
            for (int i = 0; i < recordedValues.length; i++) {
                history[i] = Long.parseLong(recordedValues[i]);
            }
            histories.add(history);
        }
    }

    public boolean isZeroDiff(int level) {
        List<Long> list = seqLists.get(level);
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i) - list.get(i - 1) != 0) {
                return false;
            }
        }
        return true;
    }

    public long nextOrPrevSeq(long[] history, int level, int part) {
        List<Long> nextSequence = new ArrayList<>();
        List<Long> prev = seqLists.get(level - 1);
        int len = history.length;
        for (int i = 1; i < len - level + 1; i++) {
            nextSequence.add(prev.get(i) - prev.get(i - 1));
        }
        seqLists.add(nextSequence);
        return nextSequence.get(part == 1 ? len - level - 1 : 0);
    }

    public long getNextOrPrev(long[] history, int part) {
        int level = 0, len = history.length;
        seqLists.add(new ArrayList<>());
        for (int i = 0; i < len; i++)
            seqLists.get(level).add(history[i]);
        long next = part == 1 ? seqLists.get(level).get(history.length - 1) : seqLists.get(level).get(0);
        boolean flip = false;
        while (!isZeroDiff(level)) {
            level++;
            if (part == 1 || flip) {
                next += nextOrPrevSeq(history, level, part);
            } else {
                next -= nextOrPrevSeq(history, level, part);
            }
            flip = !flip;
        }
        seqLists.clear();
        return next;
    }

    public void Part1() {
        long total = 0;
        for (long[] history : histories) {
            total += getNextOrPrev(history, 1);
        }

        System.out.printf("Part 1: %d\n", total);
    }

    public void Part2() {
        long total = 0;
        for (long[] history : histories) {
            total += getNextOrPrev(history, 2);
        }

        System.out.printf("Part 2: %d\n", total);
    }

    public static void main(String... args) throws Exception {
        Day09 day09 = new Day09();
        day09.allLines = Files.readAllLines(Paths.get("./day09.txt"));

        day09.SetHistory();
        day09.Part1();
        day09.Part2();
    }
}
