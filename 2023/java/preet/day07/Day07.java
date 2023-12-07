package preet.day07;

import preet.commonHelpers.FileLineReader;

import java.io.FileNotFoundException;
import java.util.*;

public class Day07 {

    private final String filePath = "2023/java/preet/day07/input.txt";

    private static class HandAndBid implements Comparable<HandAndBid> {
        @Override
        public int compareTo(HandAndBid o) {
            if (this.handType == o.handType) {
                for(int i = 0; i < this.hand.length(); i++) {
                    char curr = this.hand.charAt(i);
                    char other = o.hand.charAt(i);
                    if (map.get(curr) - map.get(other) != 0) {
                        return map.get(curr) - map.get(other);
                    }
                }
                return 0;
            }

            return this.handType.ordinal() - o.handType.ordinal();
        }
        private enum HandType {
            HighCard(),
            OnePair(),
            TwoPair(),
            ThreeOfAKind(),
            FullHouse(),
            FourOfAKind(),
            FiveOfAKind(),
            NoType();
            HandType() {
            }
        }
        private record CardAndFreq(char card, int freq) implements Comparable<CardAndFreq> {

            @Override
            public int compareTo(CardAndFreq o) {
                return Integer.compare(o.freq, this.freq);
            }
        }
        final Map<Character, Integer> map;
        private final String hand;
        private final boolean isPart2;
        private final int bid;
        private final List<CardAndFreq> freq = new ArrayList<>();
        private final HandType handType;
        final Map<Character, Integer> cards = new HashMap<>();
        private HandAndBid(String text, boolean isPart2) {
            this.isPart2 = isPart2;
            this.map = new HashMap<>(){{
                put('A', 14);
                put('K', 13);
                put('Q', 12);
                put('J', isPart2 ? 1 : 11);
                put('T', 10);
                put('9', 9);
                put('8', 8);
                put('7', 7);
                put('6', 6);
                put('5', 5);
                put('4', 4);
                put('3', 3);
                put('2', 2);
            }};
            final String[] data = text.split(" ");
            this.hand = data[0].trim();
            this.bid = Integer.parseInt(data[1].trim());
            for(char c: this.hand.toCharArray()) {
                cards.put(c, cards.getOrDefault(c, 0) + 1);
            }
            for(Map.Entry<Character, Integer> e: cards.entrySet()) {
                freq.add(new CardAndFreq(e.getKey(), e.getValue()));
            }
            Collections.sort(freq);
            this.handType = getHandType();
        }

        private HandType getHandType() {
            int numWildCards = (isPart2) ? cards.getOrDefault('J', 0) : 0;

            if (freq.size() == 1) {
                return HandType.FiveOfAKind;
            }

            if (freq.size() == 2) {
                if (numWildCards > 0) {
                    return HandType.FiveOfAKind;
                }
                if (freq.get(0).freq == 4) {
                    return HandType.FourOfAKind;
                }
                return HandType.FullHouse;
            }

            if (freq.size() == 3) {
                if (freq.get(0).freq == 3) {
                    if (numWildCards == 3 || numWildCards == 1) {
                        return HandType.FourOfAKind;
                    }
                    return HandType.ThreeOfAKind;
                }
                if (numWildCards == 2) {
                    return HandType.FourOfAKind;
                }
                if (numWildCards == 1) {
                    return HandType.FullHouse;
                }
                return HandType.TwoPair;
            }

            if (freq.size() == 4) {
                if (numWildCards > 0) {
                    return HandType.ThreeOfAKind;
                }
                return HandType.OnePair;
            }

            if(freq.size() == 5) {
                if (numWildCards > 0) {
                    return HandType.OnePair;
                }
                return HandType.HighCard;
            }

            return HandType.NoType;
        }

        public int getScoreBasedOnRank(int rank) {
            return rank * bid;
        }

    }

    public static void main(String[] args) throws FileNotFoundException {
        final Day07 day07 = new Day07();
        day07.partOne();
        day07.partTwo();
    }

    public void partOne() throws FileNotFoundException {
        final FileLineReader fileLineReader = new FileLineReader(filePath);
        long totalPoints = 0;
        final List<HandAndBid> all = new ArrayList<>();
        for(String line: fileLineReader) {
            all.add(new HandAndBid(line, false));
        }
        Collections.sort(all);
        for(int i = 1; i <= all.size(); i++) {
            totalPoints += all.get(i-1).getScoreBasedOnRank(i);
        }
        System.out.println(totalPoints);
    }

    public void partTwo() throws FileNotFoundException {
        final FileLineReader fileLineReader = new FileLineReader(filePath);
        long totalPoints = 0;
        final List<HandAndBid> all = new ArrayList<>();
        for(String line: fileLineReader) {
            all.add(new HandAndBid(line, true));
        }
        Collections.sort(all);
        for(int i = 1; i <= all.size(); i++) {
            totalPoints += ((long) i * all.get(i-1).bid);
        }
        System.out.println(totalPoints);
    }
}
