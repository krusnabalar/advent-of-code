package krusna.day08;

import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day08 {
    Queue<Character> steps = new LinkedList<>();
    Map<String, Node> idToNode = new HashMap<>();

    // p1
    String startingNodeId = "AAA";
    String destNodeId = "ZZZ";

    // p2
    List<Node> endsWithA = new ArrayList<>();
    Set<Node> endsWithZ = new HashSet<>();
    List<Node> currNodesToProcess = new ArrayList<>();
    int p1Count = 0;

    public class Node {
        public String id;
        public long stepsToZ;
        Node left;
        Node right;
        Node currNodeInPath;

        public void setLeft(Node left) {
            this.left = left;
        }

        public void setRight(Node right) {
            this.right = right;
        }

        public void setCurrNode(Node currNodeInPath) {
            this.currNodeInPath = currNodeInPath;
        }

        public Node getLeft() {
            return this.left;
        }

        public Node getRight() {
            return this.right;
        }

        public Node(String id) {
            this.id = id;
            this.stepsToZ = 0;
            this.currNodeInPath = this;
        }
    }

    public void setInstructions(String line) {
        for (char step : line.toCharArray()) {
            steps.add(step);
        }
    }

    public void setNodes(List<String> lines) {
        Pattern pattern = Pattern.compile("([A-Z]+)\\s*=\\s*\\(([A-Z]+),\\s*([A-Z]+)\\)");
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                // get id's from string
                String id = matcher.group(1), leftId = matcher.group(2), rightId = matcher.group(3);
                // set node, node left, and node right
                Node node = idToNode.containsKey(id) ? idToNode.get(id) : new Node(id);
                if (id == leftId) {
                    node.setLeft(node);
                } else {
                    Node leftNode = idToNode.containsKey(leftId) ? idToNode.get(leftId) : new Node(leftId);
                    node.setLeft(leftNode);
                    idToNode.put(leftId, leftNode);
                }
                if (id == rightId) {
                    node.setRight(node);
                } else {
                    Node rightNode = idToNode.containsKey(rightId) ? idToNode.get(rightId) : new Node(rightId);
                    node.setRight(rightNode);
                    idToNode.put(rightId, rightNode);
                }
                if (id.charAt(2) == 'A') {
                    endsWithA.add(node);
                    currNodesToProcess.add(node);
                }
                if (id.charAt(2) == 'Z') {
                    endsWithZ.add(node);
                }
                // save to node map
                idToNode.put(id, node);
            } else {
                System.out.println("No match found.");
            }
        }
    }

    public static void main(String... args) throws Exception {
        List<String> allLines = Files.readAllLines(Paths.get("./day08.txt"));
        Day08 day08 = new Day08();

        day08.setInstructions(allLines.get(0));
        day08.setNodes(allLines.subList(2, allLines.size()));
        day08.Part1();
        day08.Part2();
    }

    public void getToZ(Node node, int part, Node end) {
        Node curr = node;
        while (part == 1 ? (curr != end) : !endsWithZ.contains(curr)) {
            if (steps.poll() == 'L') {
                curr = curr.getLeft();
                steps.add('L');
            } else {
                curr = curr.getRight();
                steps.add('R');
            }
            if (part == 1) {
                p1Count++;
            } else {
                node.stepsToZ++;
            }
        }
        return;
    }

    public void Part1() {
        Node curr = idToNode.get(startingNodeId);
        Node end = idToNode.get(destNodeId);
        getToZ(curr, 1, end);
        System.out.printf("Part 1: %d\n", p1Count);
    }

    public void Part2() throws InterruptedException {
        getToZ(endsWithA.get(0), 2, null);
        long total = endsWithA.get(0).stepsToZ;
        for (int i = 1; i < endsWithA.size(); i++) {
            Node node = endsWithA.get(i);
            getToZ(node, 2, null);
            long max = Math.max(total, node.stepsToZ);
            long min = Math.min(total, node.stepsToZ);
            long lcm = max;
            while (lcm % min != 0) {
                lcm += max;
            }
            total = lcm;
        }

        System.out.printf("Part 2: %d\n", total);
    }
}
