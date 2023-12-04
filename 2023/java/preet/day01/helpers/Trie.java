package preet.day01.helpers;

import java.util.HashMap;
import java.util.Map;

public class Trie {

    private static final Map<String, Integer> numToWords =  new HashMap<>(){{
        put("zero", 0);
        put("one", 1);
        put("two", 2);
        put("three", 3);
        put("four", 4);
        put("five", 5);
        put("six", 6);
        put("seven", 7);
        put("eight", 8);
        put("nine", 9);
    }};

    public Trie[] children = new Trie[26];
    public boolean isWord = false;

    public void addWord(String word) {
        Trie curr = this;
        for(char c: word.toCharArray()) {
            int idx = c - 'a';
            if (curr.children[idx] == null) {
                curr.children[idx] = new Trie();
            }
            curr = curr.children[idx];
        }
        curr.isWord = true;
    }

    public Integer searchForward(String s, int startIdx) {
        Trie curr = this;
        StringBuilder wordSoFar = new StringBuilder();
        while (startIdx < s.length()) {
            if (Character.isDigit(s.charAt(startIdx)) || curr.children[s.charAt(startIdx) - 'a'] == null) {
                return null;
            }
            curr = curr.children[s.charAt(startIdx) - 'a'];
            wordSoFar.append(s.charAt(startIdx));
            if (curr.isWord) {
                return numToWords.get(wordSoFar.toString());
            }
            startIdx++;
        }
        return null;
    }

    public Integer searchBackward(String s, int startIdx) {
        Trie curr = this;
        StringBuilder wordSoFar = new StringBuilder();
        while (startIdx >= 0) {
            if (Character.isDigit(s.charAt(startIdx)) || curr.children[s.charAt(startIdx) - 'a'] == null) {
                return null;
            }
            curr = curr.children[s.charAt(startIdx) - 'a'];
            wordSoFar.append(s.charAt(startIdx));
            if (curr.isWord) {
                return numToWords.get(wordSoFar.reverse().toString());
            }
            startIdx--;
        }
        return null;
    }
}

