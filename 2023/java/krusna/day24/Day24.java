package krusna.day24;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day24 {
    List<String> allLines;
    List<Hailstone> hailstones = new ArrayList<>();
    long minVal = 200000000000000L;
    long maxVal = 400000000000000L;

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
        // vy * x - vx * y  = vy * x0 - vx * y0
        double a1 = h1.v.yVel, a2 = h2.v.yVel;
        double b1 = -h1.v.xVel, b2 = -h2.v.xVel;
        double det = a1 * b2 - a2 * b1;
        if (det == 0) {
            return false;
        }
        double c1 = a1 * h1.startPos.xPos + b1 * h1.startPos.yPos;
        double c2 = a2 * h2.startPos.xPos + b2 * h2.startPos.yPos;
        double x = (b2 * c1 - b1 * c2) / det;
        double y = (a1 * c2 - a2 * c1) / det;

        if (x < minVal || x > maxVal || y < minVal || y > maxVal) {
            return false;
        }

        double t1 = (x - h1.startPos.xPos) / h1.v.xVel;
        double t2 = (x - h2.startPos.xPos) / h2.v.xVel;
        return t1 >= 0 && t2 >= 0;
    }

    long[] intersectingRock() {
        double[][] matrix = new double[6][6];
        double[] constants = new double[6];
        for (int i = 0; i < 2; i++) {
            Hailstone h1 = hailstones.get(i);
            Hailstone h2 = hailstones.get(i + 1);
            long x1_0 = h1.startPos.xPos, y1_0 = h1.startPos.yPos, z1_0 = h1.startPos.zPos;
            long x2_0 = h2.startPos.xPos, y2_0 = h2.startPos.yPos, z2_0 = h2.startPos.zPos;
            long vx_1 = h1.v.xVel, vy_1 = h1.v.yVel, vz_1 = h1.v.zVel;
            long vx_2 = h2.v.xVel, vy_2 = h2.v.yVel, vz_2 = h2.v.zVel;

            // Equation 1: (Ry - H1y)(Rvx - H1vx) = (Rx - H1x)(Rvy - H1vy)
            // Equation 2: (Rz - H1z)(Rvx - H1vx) = (Rx - H1x)(Rvz - H1vz)
            // Equation 3: (Rz - H1z)(Rvy - H1vy) = (Ry - H1y)(Rvz - H1vz)

            // Eq1
            matrix[i*3][0] = vy_2 - vy_1;
            matrix[i*3][1] = vx_1 - vx_2;
            matrix[i*3][3] = y1_0 - y2_0;
            matrix[i*3][4] = x2_0 - x1_0;
            constants[i*3] = y1_0 * vx_1 - x1_0 * vy_1 - y2_0 * vx_2 + x2_0 * vy_2;

            // Eq2
            matrix[i*3+1][0] = vz_2 - vz_1;
            matrix[i*3+1][2] = vx_1 - vx_2;
            matrix[i*3+1][3] = z1_0 - z2_0;
            matrix[i*3+1][5] = x2_0 - x1_0;
            constants[i*3+1] = z1_0 * vx_1 - x1_0 * vz_1 - z2_0 * vx_2 + x2_0 * vz_2;

            // Eq3
            matrix[i*3+2][1] = vz_2 - vz_1;
            matrix[i*3+2][2] = vy_1 - vy_2;
            matrix[i*3+2][4] = z1_0 - z2_0;
            matrix[i*3+2][5] = y2_0 - y1_0;
            constants[i*3+2] = z1_0 * vy_1 - y1_0 * vz_1 - z2_0 * vy_2 + y2_0 * vz_2;
        }

        return gaussianElimination(matrix, constants);
    }

    private long[] gaussianElimination(double[][] matrix, double[] constants) {
        int n = constants.length;
        for (int i = 0; i < n; i++) {
            int maxElement = i;
            for (int j = i + 1; j < n; j++) {
                if (Math.abs(matrix[j][i]) > Math.abs(matrix[maxElement][i])) {
                    maxElement = j;
                }
            }

            double[] temp = matrix[i];
            matrix[i] = matrix[maxElement];
            matrix[maxElement] = temp;
            double t = constants[i];
            constants[i] = constants[maxElement];
            constants[maxElement] = t;

            for (int j = i + 1; j < n; j++) {
                double factor = matrix[j][i] / matrix[i][i];
                constants[j] -= factor * constants[i];
                for (int k = i; k < n; k++) {
                    matrix[j][k] -= factor * matrix[i][k];
                }
            }
        }

        long[] result = new long[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < n; j++) {
                sum += matrix[i][j] * result[j];
            }
            result[i] = (long) ((constants[i] - sum) / matrix[i][i]);
        }
        return result;
    }

    public static void main(String... args) throws Exception {
        Day24 day24 = new Day24();
        day24.allLines = Files.readAllLines(Paths.get("./2023/java/krusna/day24/day24.txt"));
        day24.setupHailstones();
        long[] rockPosition = day24.intersectingRock();

        System.out.printf("Part 1: %d\n", day24.intersectingHailstones());
        System.out.printf("Part 2: %d\n", rockPosition[0] + rockPosition[1] + rockPosition[2]);
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
