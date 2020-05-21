package tictactoegame.bots;

import tictactoegame.gameboard.GameBoard;
import tictactoegame.gameboard.Players;

import java.util.Random;

public class MediumBot extends Players {

    String playerName;
    int botWins;

    public MediumBot(String botName) {
        setPLayerName(botName);
    }

    public void setPLayerName(String botName) {
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
        String next2Move;
        GameBoard gameBoardTest = GameBoard.newInstance(gameBoard);
        int[][] availableCells = gameBoardTest.getAvailableCells();

        for (int i = 0; i < gameBoardTest.getPlayableMoves(); i++) {
            gameBoardTest.playMove(availableCells[i]);
            state = gameBoardTest.getState();

            if (!"Draw".equals(state) && !"Not finished".equals(state)) {
                System.out.println("Winning move played.");
                gameBoardTest = GameBoard.newInstance(gameBoard);
                return availableCells[i];
            } else if ("Draw".equals(state)) {
                gameBoardTest = GameBoard.newInstance(gameBoard);
                return availableCells[i];
            } else {
                gameBoardTest = GameBoard.newInstance(gameBoard);
            }
        } // to check if a win move is possible.

        for (int i = 0; i < gameBoardTest.getPlayableMoves(); i++) {
            if (gameBoardTest.getPlayableMoves() % 2 == 0) {
                next2Move = "X";
            } else {
                next2Move = "O";
            }
            gameBoardTest.playMove(availableCells[i], next2Move);
            state = gameBoardTest.getState();
            if (!"Draw".equals(state) && !"Not finished".equals(state)) {
                gameBoardTest = GameBoard.newInstance(gameBoard);
                System.out.println("Blocker move played.");
                return availableCells[i];
            } else {
                gameBoardTest = GameBoard.newInstance(gameBoard);
            }

        } // to play a block move.

        System.out.println("Random move played.");
        return randomCoordinates(gameBoardTest);

    }

    public int[] randomCoordinates(GameBoard gameBoard) {

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
