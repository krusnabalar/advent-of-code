package preet.day03;

import preet.commonHelpers.FileLineReader;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day03 {

    private final Set<Character> symbols = new HashSet<>();
    private final List<String> matrix = new ArrayList<>();

    private final String filePath = "2023/java/preet/day03/input.txt";

    public Day03() throws FileNotFoundException {
        final FileLineReader fileLineReader = new FileLineReader(filePath);
        for(String line: fileLineReader) {
            matrix.add(line);
            for(char c: line.toCharArray()) {
                if (!Character.isDigit(c) && c != '.') {
                    symbols.add(c);
                }
            }
        }
    }
    public static void main(String[] args) throws FileNotFoundException {
        final Day03 day03 = new Day03();
        day03.partOne();
        day03.partTwo();
    }

    public void partOne() throws FileNotFoundException {
        long sum = 0;
        for(int i = 0; i < matrix.size(); ) {
            String curr = matrix.get(i);
            int j = 0;
            while(j < curr.length()) {
                if (Character.isDigit(curr.charAt(j))) {
                    int start = j;
                    StringBuilder sb = new StringBuilder();
                    while(j < curr.length() && Character.isDigit(curr.charAt(j))) {
                        sb.append(curr.charAt(j));
                        j++;
                    }
                    int end = j-1;
                    if (includeNumber(matrix, start, end, i)) {
                        sum += Long.parseLong(sb.toString());
                    }
                } else {
                    j++;
                }
            }
            i++;
        }
        System.out.println(sum);
    }

    private boolean includeNumber(List<String> matrix, int start, int end, int row) {
        start--;
        end++;
        return searchRow(matrix, start, end, row - 1)
                || searchRow(matrix, start, end, row)
                || searchRow(matrix, start, end, row + 1);

    }

    private boolean searchRow(List<String> matrix, int start, int end, int row) {
        if (row < 0 || row >= matrix.size()) {
            return false;
        }
        start = Math.max(0, start);
        end = Math.min(end, matrix.size() - 1);
        String curr = matrix.get(row).substring(start, end+1);
        for(char c: curr.toCharArray()) {
            if (symbols.contains(c)) {
                return true;
            }
        }
        return false;
    }

    public void partTwo() {
        long sum = 0;
        for(int i = 0; i < matrix.size(); i++) {
            final String curr = matrix.get(i);
            for(int j = 0; j < curr.length(); j++) {
                if (curr.charAt(j) == '*') {
                    List<Integer> getNumsInVicinity = getNumsInVicinity(i, j);
                    if (getNumsInVicinity.size() == 2) {
                         sum += getNumsInVicinity.stream().reduce(1, (mul, elem) -> mul*elem);
                    }
                }
            }
        }
        System.out.println(sum);
    }

    public List<Integer> getNumsInVicinity(int x, int y) {
        List<Integer> result = new ArrayList<>();
        Set<String> checked = new HashSet<>();

        // check top left
        getNums(result, x-1,y-1, checked);

        // check top
        getNums(result, x-1,y, checked);

        // check top right
        getNums(result, x-1,y+1, checked);

        // check left
        getNums(result, x,y-1, checked);

        // check right
        getNums(result, x,y+1, checked);

        // check bottom left
        getNums(result, x+1,y-1, checked);

        // check bottom
        getNums(result, x+1,y, checked);

        // check bottom right
        getNums(result, x+1,y+1, checked);

        return result;
    }

    public void getNums(List<Integer> result, Integer x, Integer y, Set<String> checked) {
        if (x < 0 || x >= matrix.size() || y < 0 || y >= matrix.get(x).length() || checked.contains(x + "," + y)) {
            return;
        }

        String currLine = matrix.get(x);
        if (!Character.isDigit(currLine.charAt(y))) {
            return;
        }
        StringBuilder num = new StringBuilder();
        num.append(currLine.charAt(y));
        checked.add(x + "," + y);
        int i = y-1;
        while(i >= 0 && Character.isDigit(currLine.charAt(i))) {
            num.insert(0, currLine.charAt(i));
            checked.add(x + "," + i);
            i--;
        }
        int j = y+1;
        while(j <= currLine.length() && Character.isDigit(currLine.charAt(j))) {
            num.append(currLine.charAt(j));
            checked.add(x + "," + j);
            j++;
        }
        result.add(Integer.parseInt(num.toString()));
    }
}
