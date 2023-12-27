package krusna.day25;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day25 {
    List<String> allLines;
    List<String[]> edgesOriginal = new ArrayList<>();

    private void buildGraph() {
        for (String line : allLines) {
            String nodes[] = line.replace(":", "").split(" ");
            String src = nodes[0];
            for (int i = 1; i < nodes.length; i++) {
                edgesOriginal.add(new String[] { src, nodes[i] });
            }
        }
    }

    private int kragersMinCut() {
        List<String[]> edges = new ArrayList<>();

        // copy edges
        for (String[] edge : edgesOriginal) {
            edges.add(new String[] { edge[0], edge[1] });
        }

        Random rand = new Random();
        // contract edges until only 3 edges remain (min-cut size)
        while (edges.size() > 3) {
            // contract edge
            int randomEdge = rand.nextInt(edges.size());
            String[] contractingNodes = edges.get(randomEdge);
            String newVertex = contractingNodes[0] + contractingNodes[1];
            edges.remove(randomEdge);

            // replace vertices from original vertices with contracted vertex
            for (int i = 0; i < edges.size(); i++) {
                String[] edge = edges.get(i);
                if (contractingNodes[0].equals(edge[0]) || contractingNodes[1].equals(edge[0])) {
                    edge[0] = newVertex;
                }
                if (contractingNodes[0].equals(edge[1]) || contractingNodes[1].equals(edge[1])) {
                    edge[1] = newVertex;
                }
                if (edge[0].equals(edge[1])) {
                    edges.remove(i);
                    i--;
                }
            }
        }

        // check if there are 2 unique vertices
        Set<String> uniqueNodes = new HashSet<>();
        for (String[] edge : edges) {
            uniqueNodes.add(edge[0]);
            uniqueNodes.add(edge[1]);
        }
        if (uniqueNodes.size() != 2) {
            return -1;
        }

        // multiply size of the 2 contracted vertices
        int key = edges.get(0)[0].length();
        int values = edges.get(0)[1].length();
        return (key / 3) * (values / 3);
    }

    private void part1() {
        int res = -1;
        // repeat until the monte carlo method gives a valid output
        while (res < 0) {
            res = kragersMinCut();
        }
        System.out.printf("Part 1: %d\n", res);
    }

    public static void main(String... args) throws Exception {
        Day25 day25 = new Day25();
        day25.allLines = Files.readAllLines(Paths.get("./day25.txt"));
        day25.buildGraph();
        day25.part1();
    }
}
