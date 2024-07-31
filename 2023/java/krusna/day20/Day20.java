package krusna.day20;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day20 {
    List<String> allLines;
    Map<String, Module> modules = new HashMap<>();
    Queue<Pulse> pulseQueue = new LinkedList<>();
    long lowPulses = 0, highPulses = 0;
    String finalConjunctionModuleIdx;
    Map<String, Long> cycleLengths = new HashMap<>();

    void setup() {
        for (String line : allLines) {
            String[] splitInstruction = line.split(" -> ");
            String idx = splitInstruction[0];
            String[] destList = splitInstruction[1].split(", ");

            Module module;
            if (idx.equals("broadcaster")) {
                module = new BroadcasterModule(idx, destList);
            } else if (idx.startsWith("%")) {
                idx = idx.substring(1);
                module = new FlipFlopModule(idx, destList);
            } else if (idx.startsWith("&")) {
                idx = idx.substring(1);
                module = new ConjunctionModule(idx, destList);
            } else {
                throw new IllegalArgumentException("Unknown module type: " + idx);
            }
            modules.put(idx, module);
        }

        for (Module module : modules.values()) {
            for (String dest : module.destinations) {
                Module destModule = modules.get(dest);
                if (destModule instanceof ConjunctionModule) {
                    ((ConjunctionModule) destModule).inputs.put(module.name, false);
                }
                if (dest.equals("rx")) {
                    finalConjunctionModuleIdx = module.name;
                }
            }
        }

        ConjunctionModule finalConjunctionModule = (ConjunctionModule) modules.get(finalConjunctionModuleIdx);

        for (String input : finalConjunctionModule.inputs.keySet()) {
            cycleLengths.put(input, 0L);
        }
    }

    void pressButton(long pressCount) {
        pulseQueue.add(new Pulse("button", "broadcaster", false));

        while (!pulseQueue.isEmpty()) {
            Pulse pulse = pulseQueue.poll();
            if (pulse.isHigh) {
                highPulses++;
            } else {
                lowPulses++;
            }

            if (pulse.destination.equals(finalConjunctionModuleIdx) && pulse.isHigh) {
                if (cycleLengths.containsKey(pulse.source) && cycleLengths.get(pulse.source) == 0) {
                    cycleLengths.put(pulse.source, pressCount);
                }
            }

            Module module = modules.get(pulse.destination);
            if (module != null) {
                module.receivePulse(pulse.source, pulse.isHigh);
            }
        }

    }

    long lcm(long a, long b) {
        return a * (b / gcd(a, b));
    }

    long gcd(long a, long b) {
        while (b > 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    public class Pulse {
        String source;
        String destination;
        boolean isHigh;

        Pulse(String source, String destination, boolean isHigh) {
            this.source = source;
            this.destination = destination;
            this.isHigh = isHigh;
        }
    }

    abstract class Module {
        String name;
        String[] destinations;

        Module(String name, String[] destinations) {
            this.name = name;
            this.destinations = destinations;
        }

        abstract void receivePulse(String source, boolean isHigh);

        void sendPulse(boolean isHigh) {
            for (String dest : destinations) {
                pulseQueue.add(new Pulse(name, dest, isHigh));
            }
        }
    }

    public class BroadcasterModule extends Module {
        BroadcasterModule(String name, String[] destinations) {
            super(name, destinations);
        }

        @Override
        void receivePulse(String source, boolean isHigh) {
            sendPulse(isHigh);
        }
    }

    public class FlipFlopModule extends Module {
        boolean isOn = false;

        FlipFlopModule(String name, String[] destinations) {
            super(name, destinations);
        }

        @Override
        void receivePulse(String source, boolean isHigh) {
            if (!isHigh) {
                isOn = !isOn;
                sendPulse(isOn);
            }
        }
    }

    public class ConjunctionModule extends Module {
        Map<String, Boolean> inputs = new HashMap<>();

        ConjunctionModule(String name, String[] destinations) {
            super(name, destinations);
        }

        @Override
        void receivePulse(String source, boolean isHigh) {
            inputs.put(source, isHigh);
            boolean allHigh = inputs.values().stream().allMatch(b -> b);
            sendPulse(!allHigh);
        }
    }

    void part1() {
        for (int i = 0; i < 1000; i++) {
            pressButton(0);
        }
        System.out.printf("Part 1: %d\n", lowPulses * highPulses);
    }

    void part2() {
        long count = 0;
        long res = 1;

        while (cycleLengths.values().stream().anyMatch(v -> v == 0)) {
            count++;
            pressButton(count);
        }

        for (long length : cycleLengths.values()) {
            res = lcm(res, length);
        }

        System.out.printf("Part 2: %d\n", res);
    }

    public static void main(String... args) throws Exception {
        Day20 day20 = new Day20();
        day20.allLines = Files.readAllLines(Paths.get("./2023/java/krusna/day20/day20.txt"));
        day20.setup();
        day20.part1();
        day20.part2();
    }
}
