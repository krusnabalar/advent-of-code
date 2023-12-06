import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day06 {
    private static int findMarkerStart(int markerSize, String datastreamBuffer) {
        int bufferLength = datastreamBuffer.length();
        Set<Character> uniqueChars = new HashSet<>();
        for (int i = 0; i < bufferLength; i++) {
            windowLoop: for (int window = i; window < i + markerSize; window++) {
                if (!uniqueChars.add(datastreamBuffer.charAt(window))) {
                    uniqueChars.clear();
                    break windowLoop;
                }
                if (window == i + markerSize - 1) {
                    return i + markerSize;
                }
            }
        }
        return -1;
    }

    public static void main(String... args) throws Exception {
        List<String> allLines = Files.readAllLines(Paths.get("../input/day6.txt"));
        String datastreamBuffer = allLines.get(0);
        int startMarkerSize = 4, messageMarkerSize = 14;
        int startOfPacketMarker = findMarkerStart(startMarkerSize, datastreamBuffer);
        int startOfMessageMarker = findMarkerStart(messageMarkerSize, datastreamBuffer);

        System.out.printf("part 1: %d\n", startOfPacketMarker);
        System.out.printf("part 2: %d\n", startOfMessageMarker);
    }
}
