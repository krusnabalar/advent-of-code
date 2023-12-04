package preet.day02;

import preet.commonHelpers.FileLineReader;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Day02 {

    private class GameState {
        public int red;
        public int blue;
        public int green;

        public boolean isPossibleWithInitialState(final GameState init) {
            return this.red <= init.red && this.blue <= init.blue && this.green <= init.green;
        }

    }

    private class GameStateCreator {
        public GameState getGameState(String game) {
            final GameState gameState = new GameState();
            game = game.trim();
            final String[] val = game.split(",");
            for(int i = 0; i < val.length; i++) {
                val[i] = val[i].trim();
                final String[] gameValues = val[i].split(" ");
                final String color = gameValues[1];
                switch (color) {
                    case "red":
                        gameState.red = Integer.parseInt(gameValues[0]);
                        break;
                    case "blue":
                        gameState.blue = Integer.parseInt(gameValues[0]);
                        break;
                    case "green":
                        gameState.green = Integer.parseInt(gameValues[0]);
                        break;
                }
            }
            return gameState;
        }
    }

    public class GameLine {
        private final String gameLine;
        public GameLine(String line) {
            this.gameLine = line;
        }

        public long getGameId() {
            return Long.parseLong(gameLine.split(":")[0].split(" ")[1]);
        }

        public List<String> getGameStates() {
            final String stringWithGameStates = gameLine.split(":")[1].trim();
            final String[] gameStates = stringWithGameStates.split(";");
            final List<String> result = new ArrayList<>();
            for(String gameState: gameStates) {
                result.add(gameState.trim());
            }
            return result;
        }
    }

    private static final String inputFilePath = "2023/java/preet/day02/input.txt";
    public static void main(String[] args) throws FileNotFoundException {
        final Day02 day02 = new Day02();
        day02.partOne();
        day02.partTwo();
    }

    public void partOne() throws FileNotFoundException {
        final FileLineReader reader = new FileLineReader(inputFilePath);
        final GameState init = new GameState();
        final GameStateCreator gameStateCreator = new GameStateCreator();
        init.red = 12;
        init.green = 13;
        init.blue = 14;

        long sum = 0;

        for(String line: reader) {
            final GameLine gameLine = new GameLine(line);
            List<String> gameStates = gameLine.getGameStates();
            final boolean matched = gameStates.stream().allMatch((s) -> {
                final GameState currGameState = gameStateCreator.getGameState(s);
                return currGameState.isPossibleWithInitialState(init);
            });
            if (matched) {
                sum += gameLine.getGameId();
            }
        }
        System.out.println(sum);
    }

    public void partTwo() throws FileNotFoundException {
        final FileLineReader reader = new FileLineReader(inputFilePath);
        final GameState init = new GameState();
        final GameStateCreator gameStateCreator = new GameStateCreator();
        init.red = 12;
        init.green = 13;
        init.blue = 14;

        long sum = 0;

        for(String line: reader) {
            final GameLine gameLine = new GameLine(line);
            List<String> gameStates = gameLine.getGameStates();
            List<GameState> gameStateList = gameStates.stream().map(gameStateCreator::getGameState).toList();
            int maxRed = gameStateList.stream().max((a, b) -> Integer.compare(a.red, b.red)).get().red;
            int maxGreen = gameStateList.stream().max((a, b) -> Integer.compare(a.green, b.green)).get().green;
            int maxBlue = gameStateList.stream().max((a, b) -> Integer.compare(a.blue, b.blue)).get().blue;
            long power = maxRed * maxGreen * maxBlue;
            sum += power;
        }
        System.out.println(sum);
    }
}
