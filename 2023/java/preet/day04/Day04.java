package preet.day04;

import preet.commonHelpers.FileLineReader;

import java.io.FileNotFoundException;
import java.util.*;

public class Day04 {

    private final String filePath = "2023/java/preet/day04/input.txt";

    public static class ScratchPlay {
        public int cardNum;

        public int numMatches = 0;
        public int numPoints = 0;

        public ScratchPlay(final String line) {

            cardNum = Integer.parseInt(line.split(":")[0].trim().split("Card")[1].trim());

            final String winningNumbersAndCard = line.split(":")[1].trim();

            final String winningNumberLine = winningNumbersAndCard.split("\\|")[0].trim();
            final String[] winningNumbers = winningNumberLine.split(" ");
            final Set<Integer> winningNums = new HashSet<>();
            for(String num: winningNumbers) {
                num = num.trim();
                if (!num.isBlank() && !num.isEmpty()) {
                    winningNums.add(Integer.parseInt(num));
                }
            }

            final String scratchCardNumbersLine = winningNumbersAndCard.split("\\|")[1].trim();
            final String[] scratchCardNumbers = scratchCardNumbersLine.split(" ");
            final List<Integer> cardNums = new ArrayList<>();
            for(String num: scratchCardNumbers) {
                num = num.trim();
                if (!num.isBlank() && !num.isEmpty()) {
                    cardNums.add(Integer.parseInt(num));
                }
            }

            for(int i: cardNums) {
                if (winningNums.contains(i)) {
                    numMatches++;
                }
            }

            if (numMatches > 0) {
                 numPoints = (int) Math.pow(2, (double) numMatches-1);
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        final Day04 day04 = new Day04();
        day04.partOne();
        day04.partTwo();
    }

    public void partOne() throws FileNotFoundException {
        final FileLineReader fileLineReader = new FileLineReader(filePath);
        long totalPoints = 0;
        for(String line: fileLineReader) {
           totalPoints += new ScratchPlay(line).numPoints;
        }
        System.out.println(totalPoints);
    }

    public void partTwo() throws FileNotFoundException {
        final FileLineReader fileLineReader = new FileLineReader(filePath);
        final Map<Integer, ScratchPlay> scratchCards = new HashMap<>();
        int numScratchCards = 0;
        for(String line: fileLineReader) {
            final ScratchPlay sp = new ScratchPlay(line);
            scratchCards.put(sp.cardNum, sp);
        }
        final ArrayDeque<Integer> processingQueue = new ArrayDeque<>(scratchCards.keySet());
        while(!processingQueue.isEmpty()) {
            int cardNum = processingQueue.poll();
            numScratchCards++;
            final ScratchPlay card = scratchCards.get(cardNum);
            if (card.numMatches > 0) {
                for(int i = cardNum+1; i <= cardNum+card.numMatches; i++) {
                    processingQueue.add(i);
                }
            }
        }

        System.out.println(numScratchCards);
    }
}
