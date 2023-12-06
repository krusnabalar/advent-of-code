import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

class Day02 {
    public static void main(String... args) throws Exception {
        List<String> allLines = Files.readAllLines(Paths.get("../input/day2.txt"));
        int myScore = 0;
        int myActualScore = 0;

        // create a map indicating scores for each hand played
        Map<Character, Integer> scoreVal = new HashMap<>();
        scoreVal.put('X', 1);
        scoreVal.put('Y', 2);
        scoreVal.put('Z', 3);
        scoreVal.put('A', 1);
        scoreVal.put('B', 2);
        scoreVal.put('C', 3);

        // create a map to show winning hands
        Map<Character, Character> winningHand = new HashMap<>();
        winningHand.put('X', 'C');
        winningHand.put('Y', 'A');
        winningHand.put('Z', 'B');
        winningHand.put('A', 'B');
        winningHand.put('B', 'C');
        winningHand.put('C', 'A');

        // create a map to show losing hands
        Map<Character, Character> losingHand = new HashMap<>();
        losingHand.put('X', 'B');
        losingHand.put('Y', 'C');
        losingHand.put('Z', 'A');
        losingHand.put('A', 'C');
        losingHand.put('B', 'A');
        losingHand.put('C', 'B');

        for (String line : allLines) {
            char opponentPlay = line.charAt(0);
            char myPlay = line.charAt(2);

            myScore += scoreVal.get(myPlay);
            if (opponentPlay == winningHand.get(myPlay)) {
                myScore += 6;
            } else if (opponentPlay == losingHand.get(myPlay)) {
                myScore += 0;
            } else {
                myScore += 3;
            }

            if (myPlay == 'X') {
                myActualScore += scoreVal.get(losingHand.get(opponentPlay));
            } else if (myPlay == 'Y') {
                myActualScore += 3;
                myActualScore += scoreVal.get(opponentPlay);
            } else {
                myActualScore += 6;
                myActualScore += scoreVal.get(winningHand.get(opponentPlay));
            }
        }

        System.out.printf("part 1: %d\n", myScore);
        System.out.printf("part 2: %d\n", myActualScore);
    }
}
