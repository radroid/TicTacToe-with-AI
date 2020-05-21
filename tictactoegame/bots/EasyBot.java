package tictactoegame.bots;

import tictactoegame.gameboard.GameBoard;
import tictactoegame.gameboard.Players;

import java.util.Random;

public class EasyBot extends Players {

    String playerName;
    int botWins;

    public EasyBot(String botName) {
        setPlayerName(botName);
    }

    public void setPlayerName(String botName) {
        this.playerName = botName;
    }
    public String getPlayerName() {
        return playerName;
    }

    public int getWins() {
        return botWins;
    }
    public void recordWin() {
        this.botWins++;
    }

    public int[] getCoordinates(GameBoard gameBoard) {

        System.out.printf("%s is making move.%n", getPlayerName());

        Random random = new Random();
        int[] coordinates = new int[2];
        boolean available = false;
        int rowNum = 0, colNum = 0;

        while (!available) {
            rowNum = random.nextInt(3);
            colNum = random.nextInt(3);
            available = gameBoard.getCurrentBoard()[rowNum][colNum].equals("_");
        }
        coordinates[0] = rowNum;
        coordinates[1] = colNum;
        return coordinates;
    } // selects a random set of available coordinates and returns.
}
