package preet.day01;

import preet.day01.helperClasses.FileLineReader;
import preet.day01.helperClasses.Trie;

import java.io.FileNotFoundException;

public class Day01 {

    private static final String inputFilePath = "";
    public static void main(String[] args) throws FileNotFoundException {
        partOne();
        partTwo();
    }

    public static void partOne() throws FileNotFoundException {
        final FileLineReader reader = new FileLineReader(inputFilePath);
        long sum = 0;
        for(String line: reader) {
            long digitOne = 0;
            for(int i = 0; i < line.length(); i++) {
                if (Character.isDigit(line.charAt(i))) {
                    digitOne = line.charAt(i) - '0';
                    break;
                }
            }

            long digitTwo = 0;
            for(int i = line.length() - 1; i >= 0; i--) {
                if (Character.isDigit(line.charAt(i))) {
                    digitTwo = line.charAt(i) - '0';
                    break;
                }
            }

            sum += ((digitOne*10L) + digitTwo);
        }
        System.out.println(sum);
    }

    public static void partTwo() throws FileNotFoundException {

        String[] numWords =  new String[]{
                "zero",
                "one",
                "two",
                "three",
                "four",
                "five",
                "six",
                "seven",
                "eight",
                "nine",
        };

        String[] numWordsReverse = new String[numWords.length];
        for(int i = 0; i < numWords.length; i++) {
            numWordsReverse[i] = new StringBuilder(numWords[i]).reverse().toString();
        }

        Trie forward = new Trie();
        for(String word: numWords) {
            forward.addWord(word);
        }

        Trie backward = new Trie();
        for(String word: numWordsReverse) {
            backward.addWord(word);
        }





        final FileLineReader reader = new FileLineReader(inputFilePath);
        long sum = 0;

        for(String line: reader) {
            long digitOne = 0;
            for(int i = 0; i < line.length(); i++) {
                if (Character.isDigit(line.charAt(i))) {
                    digitOne = line.charAt(i) - '0';
                    break;
                } else {
                    Integer potential = forward.searchForward(line, i);
                    if (potential != null) {
                        digitOne = potential;
                        break;
                    }
                }
            }

            long digitTwo = 0;
            for(int i = line.length() - 1; i >= 0; i--) {
                if (Character.isDigit(line.charAt(i))) {
                    digitTwo = line.charAt(i) - '0';
                    break;
                } else {
                    Integer potential = backward.searchBackward(line, i);
                    if (potential != null) {
                        digitTwo = potential;
                        break;
                    }
                }
            }

            sum += ((digitOne*10L) + digitTwo);
        }

        System.out.println(sum);

    }
}
