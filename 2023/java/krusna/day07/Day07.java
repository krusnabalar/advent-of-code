package krusna.day07;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day07 {
    private class Hands {
        private String hand;
        private int typeScore;
        private int part;
        private Map<Character, Integer> cardToRank;

        public String getHand() {
            return this.hand;
        }

        public int getTypeScore() {
            return this.typeScore;
        }

        public int getCardScore(int cardNum, int part) {
            return cardToRank.get(this.hand.charAt(cardNum));
        }

        public Hands(String hand, int typeScore, int part) {
            this.hand = hand;
            this.typeScore = typeScore;
            this.part = part;
            this.cardToRank = Map.ofEntries(
                    Map.entry('A', 13),
                    Map.entry('K', 12),
                    Map.entry('Q', 11),
                    Map.entry('J', (part == 1) ? 10 : 0),
                    Map.entry('T', 9),
                    Map.entry('9', 8),
                    Map.entry('8', 7),
                    Map.entry('7', 6),
                    Map.entry('6', 5),
                    Map.entry('5', 4),
                    Map.entry('4', 3),
                    Map.entry('3', 2),
                    Map.entry('2', 1));
        }
    }

    private class HandComparator implements Comparator<Hands> {
        @Override
        public int compare(Hands firstHand, Hands secondHand) {
            int typeCompare = Integer.compare(firstHand.getTypeScore(), secondHand.getTypeScore());
            if (typeCompare != 0)
                return typeCompare;

            int handSize = firstHand.getHand().length();
            int cardCompare = 0;
            for (int i = 0; i < handSize; i++) {
                cardCompare = Integer.compare(firstHand.getCardScore(i, firstHand.part),
                        secondHand.getCardScore(i, secondHand.part));
                if (cardCompare != 0)
                    return cardCompare;
            }
            return 1;
        }
    }

    Map<String, Integer> handToBet = new HashMap<>();
    Map<String, Integer> handTypeToScore = Map.of(
            "Five of a kind", 7,
            "Four of a kind", 6,
            "Full house", 5,
            "Three of a kind", 4,
            "Two Pair", 3,
            "One Pair", 2,
            "High Card", 1);
    List<Hands> hands = new ArrayList<>();

    public static void main(String... args) throws Exception {
        List<String> allLines = Files.readAllLines(Paths.get("./day07.txt"));
        Day07 day07 = new Day07();

        day07.storeHands(allLines);
        day07.partOne();
        day07.hands.clear();
        day07.partTwo();
    }

    public void classifyHand(String hand, int part) {
        // classify hand -> map card to count
        Map<Character, Integer> cardCounts = new HashMap<>();
        for (char c : hand.toCharArray()) {
            cardCounts.put(c, cardCounts.getOrDefault(c, 0) + 1);
        }

        // go through map and classify
        String type = "";
        int jCount = cardCounts.getOrDefault('J', 0);
        if (cardCounts.containsValue(5)) {
            type = "Five of a kind";
        } else if (cardCounts.containsValue(4)) {
            if (part == 2 && jCount > 0) {
                type = "Five of a kind";
            } else {
                type = "Four of a kind";
            }
        } else if (cardCounts.containsValue(3)) {
            if (part == 2 && jCount > 0) {
                if (jCount == 3) {
                    if (cardCounts.containsValue(2)) {
                        type = "Five of a kind";
                    } else {
                        type = "Four of a kind";
                    }
                } else {
                    if (jCount == 1) {
                        type = "Four of a kind";
                    } else {
                        type = "Five of a kind";
                    }
                }
            } else {
                if (cardCounts.containsValue(2)) {
                    type = "Full house";
                } else {
                    type = "Three of a kind";
                }
            }
        } else if (cardCounts.containsValue(2)) {
            long numPairs = cardCounts.values().stream().filter(count -> count == 2).count();
            if (part == 2 && jCount > 0) {
                if (jCount == 2) {
                    if (numPairs == 2) {
                        type = "Four of a kind";
                    } else {
                        type = "Three of a kind";
                    }
                } else {
                    if (numPairs == 2) {
                        type = "Full house";
                    } else {
                        type = "Three of a kind";
                    }
                }
            } else {
                if (numPairs == 1) {
                    type = "One Pair";
                } else {
                    type = "Two Pair";
                }
            }
        } else {
            if (part == 2 && jCount > 0) {
                type = "One Pair";
            } else {
                type = "High Card";
            }
        }

        // add to hands list
        hands.add(new Hands(hand, handTypeToScore.get(type), part));
    }

    public void storeHands(List<String> allLines) {
        for (String line : allLines) {
            String[] lineArr = line.split(" ");
            // add hand and respective bet to map
            handToBet.put(lineArr[0], Integer.parseInt(lineArr[1]));
        }
    }

    public void partOne() {
        for (String hand : handToBet.keySet()) {
            classifyHand(hand, 1);
        }
        Collections.sort(hands, new HandComparator());
        int totalWinnings = 0;
        int handRank = 1;
        for (Hands hand : hands) {
            totalWinnings += handRank * handToBet.get(hand.getHand());
            handRank++;
        }

        System.out.printf("Part 1: %d\n", totalWinnings);
    }

    public void partTwo() {
        for (String hand : handToBet.keySet()) {
            classifyHand(hand, 2);
        }
        Collections.sort(hands, new HandComparator());
        int totalWinnings = 0;
        int handRank = 1;
        for (Hands hand : hands) {
            totalWinnings += handRank * handToBet.get(hand.getHand());
            handRank++;
        }

        System.out.printf("Part 2: %d\n", totalWinnings);
    }
}
