package krusna.day24;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day24 {
    List<String> allLines;
    List<Hailstone> hailstones = new ArrayList<>();
    long minVal, maxVal;

    void setupHailstones() {
        for (String line : allLines) {
            String[] parts = line.split(" @ ");
            String[] pString = parts[0].split(", ");
            String[] vString = parts[1].split(", ");
            Position p = new Position(
                Long.parseLong(pString[0]),
                Long.parseLong(pString[1]),
                Long.parseLong(pString[2]));
            Velocity v = new Velocity(
                Long.parseLong(vString[0].trim()),
                Long.parseLong(vString[1].trim()),
                Long.parseLong(vString[2].trim()));
            hailstones.add(new Hailstone(p, v));
        }
    }

    int intersectingHailstones() {
        int intersecting = 0;
        for (int i = 0; i < hailstones.size(); i++) {
            for (int j = i + 1; j < hailstones.size(); j++) {
                if (intersect2D(hailstones.get(i), hailstones.get(j))) {
                    intersecting++;
                }
            }
        }
        return intersecting;
    }

    boolean intersect2D(Hailstone h1, Hailstone h2) {
        // Paramteric:
        //      x = x_0 + v_x * t
        //      y = y_0 + v_y * t
        // Standard:
        //      (x - x_0) / v_x = (y - y_0) / v_y
        //      v_y * x - v_x * y   = v_y * x_0 - v_x * y_0
        //      a_1 * x + b_1 * y   = a_2 * x   + b_2 * y
        double a1 = h1.v.yVel, a2 = h2.v.yVel;
        double b1 = -h1.v.xVel, b2 = -h2.v.xVel;
        double det = a1 * b2 - a2 * b1;
        double c1 = a1 * h1.startPos.xPos + b1 * h1.startPos.yPos;
        double c2 = a2 * h2.startPos.xPos + b2 * h2.startPos.yPos;
        double x = (b2 * c1 - b1 * c2) / det;
        double y = (a1 * c2 - a2 * c1) / det;

        if (Math.abs(det) < 1e-9) {
            return false;
        }
        if (x < minVal || x > maxVal || y < minVal || y > maxVal) {
            return false;
        }

        double t1 = (x - h1.startPos.xPos) / h1.v.xVel;
        double t2 = (x - h2.startPos.xPos) / h2.v.xVel;
        return t1 >= 0 && t2 >= 0;
    }

    public static void main(String... args) throws Exception {
        Day24 day24 = new Day24();
        day24.minVal = 200000000000000L;
        day24.maxVal = 400000000000000L;
        day24.allLines = Files.readAllLines(Paths.get("./2023/java/krusna/day24/day24.txt"));
        day24.setupHailstones();
        System.out.printf("Part 1: %d\n", day24.intersectingHailstones());

    }
}

class Hailstone {
    Position startPos;
    Velocity v;

    public Hailstone(Position startPos, Velocity v) {
        this.startPos = startPos;
        this.v = v;
    }
}

class Position {
    long xPos, yPos, zPos;

    public Position(long xPos, long yPos, long zPos) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;
    }
}

class Velocity {
    long xVel, yVel, zVel;

    public Velocity(long xVel, long yVel, long zVel) {
        this.xVel = xVel;
        this.yVel = yVel;
        this.zVel = zVel;
    }
}
