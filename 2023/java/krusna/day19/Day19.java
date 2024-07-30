package krusna.day19;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day19 {
    List<String> allLines = new ArrayList<>();
    Map<String, List<String[]>> workflow = new HashMap<>();
    List<Map<String, Integer>> partList = new ArrayList<>();

    void setupPartOne() {
        int breakIdx = 0;
        for (int j = 0; j < allLines.size(); j++) {
            String line = allLines.get(j);
            if (line.length() == 0) {
                breakIdx = j + 1;
                break;
            }
            String key = line.split("[{]")[0];
            int startIdx = line.indexOf('{') + 1;
            int endIdx = line.length() - 1;
            String[] funcs = line.substring(startIdx, endIdx).split(",");
            List<String[]> workflowFuncs = new ArrayList<>();

            for (String f : funcs) {
                if (f.length() == 1 || f.charAt(1) > 96) {
                    workflowFuncs.add(new String[] {f});
                    continue;
                }
                String rating = f.substring(0,1);
                String condition = f.substring(1,2);
                String s = f.substring(2, f.length());
                String value = s.split(":")[0];
                String condOutput = s.split(":")[1];

                workflowFuncs.add(new String[] {rating, condition, value, condOutput});
            }
            workflow.put(key, workflowFuncs);
        }

        for (int i = breakIdx; i < allLines.size(); i++) {
            Map<String, Integer> partRatings = new HashMap<>();
            String originalLine = allLines.get(i);
            String line = originalLine.substring(1, originalLine.length() - 1);
            for (String rating : line.split(",")) {
                int ratingValue = Integer.parseInt(rating.split("=")[1]);
                partRatings.put(rating.substring(0,1), ratingValue);
            }
            partList.add(partRatings);
        }
    }

    boolean handleFuncOutput(Map<String, Integer> partRatings, String output) {
        if (output.equals("R")) {
            return false;
        } else if (output.equals("A")) {
            return true;
        } else {
            return accepted(partRatings, workflow.get(output));
        }
    }

    boolean accepted(Map<String, Integer> partRatings, List<String[]> funcs) {
        for (String[] func : funcs) {
            if (func.length == 1) {
                return handleFuncOutput(partRatings, func[0]);
            }
            if ((func[1].equals("<") && partRatings.get(func[0]) < Integer.parseInt(func[2])) ||
                (func[1].equals(">") && partRatings.get(func[0]) > Integer.parseInt(func[2]))) {
                return handleFuncOutput(partRatings, func[3]);
            }
        }
        return false;
    }

    /*
     * Part 2
     */

    Map<String, Workflow> idxToWorkflow = new HashMap<>();

    void setupPartTwo() {
        for (String s : allLines) {
            if (s.length() == 0) {
                break;
            }
            String[] nodeDetails = s.substring(0,s.length() - 1).split("\\{");
            String[] ruleDetails = nodeDetails[1].split(",");

            List<Rule> rules = new ArrayList<>();
            for (String rule : ruleDetails) {
                if (!rule.contains(":")) break;
                Rule r = new Rule(rule.charAt(0), Integer.parseInt(rule.split(":")[0].substring(2)),
                    rule.charAt(1), rule.split(":")[1]);
                rules.add(r);
            }
            Workflow w = new Workflow(nodeDetails[0], rules, ruleDetails[ruleDetails.length - 1]);
            idxToWorkflow.put(nodeDetails[0], w);
        }
    }

    long numPossibilities(Ranges r) {
        long total = 1;
        for (Range range : r.ranges.values()){
            total *= range.max - range.min + 1;
        }
        return total;
    }

    Ranges[] splitRange(Ranges ranges, Rule rule) {
        Range currRange = ranges.ranges.get(rule.part);
        if (rule.operator) {
            if (currRange.max < rule.value) {
                return new Ranges[] {ranges.copy(), null};
            } else if (currRange.min >= rule.value) {
                return new Ranges[] {null, ranges.copy()};
            } else {
                Ranges trueRanges = ranges.copy();
                Ranges falseRanges = ranges.copy();
                trueRanges.ranges.get(rule.part).max = rule.value - 1;
                falseRanges.ranges.get(rule.part).min = rule.value;
                return new Ranges[] {trueRanges, falseRanges};
            }
        } else {
            if (currRange.min > rule.value) {
                return new Ranges[] {ranges.copy(), null};
            } else if (currRange.max <= rule.value) {
                return new Ranges[] {null, ranges.copy()};
            } else {
                Ranges trueRanges = ranges.copy();
                Ranges falseRanges = ranges.copy();
                trueRanges.ranges.get(rule.part).min = rule.value + 1;
                falseRanges.ranges.get(rule.part).max = rule.value;
                return new Ranges[] {trueRanges, falseRanges};
            }
        }
    }

    long dfs(String workflowIdx, Ranges r) {
        if(workflowIdx.equals("A")) {
            return numPossibilities(r);
        } else if (workflowIdx.equals("R")) {
            return 0;
        }

        Workflow w = idxToWorkflow.get(workflowIdx);
        long total = 0;
        Ranges currRanges = r.copy();

        for (Rule rule : w.rules) {
            Ranges[] split = splitRange(currRanges, rule);
            if (split[0] != null) {
                total += dfs(rule.dest, split[0]);
            }
            if (split[1] != null) {
                currRanges = split[1];
            } else {
                break;
            }
        }

        if (currRanges != null) {
            total += dfs(w.defaultWorkflow, currRanges);
        }

        return total;
    }

    public class Workflow {
        String idx;
        List<Rule> rules;
        String defaultWorkflow;

        public Workflow(String idx, List<Rule> rules, String defaultWorkflow) {
            this.idx = idx;
            this.rules = rules;
            this.defaultWorkflow = defaultWorkflow;
        }
    }

    public class Rule {
        char part;
        int value;
        boolean operator;
        String dest;

        public Rule(char part, int value, char operator, String dest) {
            this.part = part;
            this.value = value;
            this.operator = operator == '<' ? true : false;
            this.dest = dest;
        }
    }

    public class Range {
        int min;
        int max;

        public Range(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public Range copy() {
            return new Range(this.min, this.max);
        }
    }

    public class Ranges {
        public Map<Character, Range> ranges;

        public Ranges() {
            ranges = new HashMap<>();
            ranges.put('x', new Range(1, 4000));
            ranges.put('m', new Range(1, 4000));
            ranges.put('a', new Range(1, 4000));
            ranges.put('s', new Range(1, 4000));
        }

        public Ranges copy() {
            Ranges newRanges = new Ranges();
            for (char c : "xmas".toCharArray()) {
                newRanges.ranges.put(c, this.ranges.get(c).copy());
            }
            return newRanges;
        }
    }

    void part1() {
        setupPartOne();
        int res = 0;
        for (Map<String, Integer> partRatings : partList) {
            if (accepted(partRatings, workflow.get("in"))) {
                for (int val : partRatings.values()) {
                    res += val;
                }
            }
        }
        System.out.printf("Part 1: %d\n", res);
    }

    void part2() {
        setupPartTwo();
        long res = dfs("in", new Ranges());
        System.out.printf("Part 2: %d\n", res);
    }
    public static void main(String... args) throws Exception {
        Day19 day19 = new Day19();
        day19.allLines = Files.readAllLines(Paths.get("./day19.txt"));
        day19.part1();
        day19.part2();
    }
}
