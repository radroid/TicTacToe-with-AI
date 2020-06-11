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

        String state;
        GameBoard gameBoardTest = GameBoard.newInstance(gameBoard);
        int[][] availableCells = gameBoardTest.getAvailableCells();
        for (int i = 0; i < gameBoardTest.getPlayableMoves(); i++) {
            gameBoardTest.playMove(availableCells[i]);
            state = gameBoardTest.getState();

            if (!"Draw".equals(state) && !"Not finished".equals(state)) {
                return availableCells[i]; // winning move played
            } else if ("Draw".equals(state)) {
                return availableCells[i];
            } else {
                gameBoardTest = GameBoard.newInstance(gameBoard);
            }
        } // check and return if a win move is possible.

        Random random = new Random();
        int location = random.nextInt(gameBoard.getPlayableMoves());
        return availableCells[location]; // return a random move

    } // selects a random set of available coordinates and returns.
}
