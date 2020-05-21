package mediumbot;

import java.util.Random;
import tictactoe.GameBoard;

public class MediumBot {

    boolean movePlayed = false;

    public int[] winCoordinates(GameBoard gameBoard) {
        movePlayed = false;
        String state;
        String next2Move;
        GameBoard gameBoardTest = GameBoard.newInstance(gameBoard);
        int[][] availableCells = gameBoardTest.getAvailableCells();

        for (int i = 0; i < gameBoardTest.getPlayableMoves(); i++) {
            gameBoardTest.playMove(availableCells[i]);
            state = gameBoardTest.getState();

            if (!"Draw".equals(state) && !"Not finished".equals(state)) {
                movePlayed = true;
                System.out.println("Winning move played.");
                gameBoardTest = GameBoard.newInstance(gameBoard);
                return availableCells[i];
            } else if ("Draw".equals(state)) {
                movePlayed = true;
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
                movePlayed = true;
                gameBoardTest = GameBoard.newInstance(gameBoard);
                System.out.println("Blocker move played.");
                return availableCells[i];
            } else {
                gameBoardTest = GameBoard.newInstance(gameBoard);
            }

        } // to play a block move.

        System.out.println("Random move played.");
        return randomCoordinates(gameBoardTest.getCurrentBoard());

    }

    public int[] randomCoordinates(String[][] curTable) {
        Random random = new Random();
        int[] coordinates = new int[2];
        boolean available = false;
        int rowNum = 0, colNum = 0;

        while (!available) {
            rowNum = random.nextInt(3);
            colNum = random.nextInt(3);
            available = curTable[rowNum][colNum].equals("_");
        }
        coordinates[0] = rowNum;
        coordinates[1] = colNum;
        return coordinates;
    } // selects a random set of available coordinates and returns.

    public boolean isMovePlayed() {
        return movePlayed;
    }

}
