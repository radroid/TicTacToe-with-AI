package hardbot;

import tictactoe.GameBoard;

import java.util.Arrays;

public class HardBot {

    String botPlayer;
    String anotherPlayer;

    public HardBot(String botPlayer, String anotherPlayer) {
        setBotPlayer(botPlayer);
        setAnotherPlayer(anotherPlayer);
    }

    public void setAnotherPlayer(String anotherPlayer) {
        this.anotherPlayer = anotherPlayer;
    }

    public void setBotPlayer(String botPlayer) {this.botPlayer = botPlayer;
    }

    public int[] generateBestCoordinates(GameBoard gameBoard) {
        boolean movePlayed = false;
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
        
        int[] scoreTally = new int[gameBoard.getPlayableMoves()];
        GameBoard gameBoard1 = GameBoard.newInstance(gameBoard);
        for (int i = 0; i < gameBoard.getPlayableMoves(); i++) {
            gameBoard1.playMove(gameBoard1.getAvailableCells()[i]);
            scoreTally[i] = bestMove(gameBoard1);
            //System.out.println("Number : " + i);
            //gameBoard1.printGameBoard();
            //gameBoard.printGameBoard();
            gameBoard1 = GameBoard.newInstance(gameBoard);
            //gameBoard1.printGameBoard();
        }
        int result = 0;
        //System.out.println(Arrays.toString(scoreTally));
        for (int i = 0; i < scoreTally.length; i++) {
            if (scoreTally[i] > scoreTally[result]) {
                result = i;
            }
        }
        //System.out.println(result);
        System.out.println(Arrays.toString(gameBoard.getAvailableCells()[result]));
        return gameBoard.getAvailableCells()[result];
    }

    /*public int miniMax(GameBoard gameBoardHardInternal, int index) {
        GameBoard gameBoard =  GameBoard.newInstance(gameBoardHardInternal);
        int[][] availableCells = gameBoardHardInternal.getAvailableCells();
        String state = gameBoardHardInternal.getState();
        //System.out.println(gameBoardHardInternal.getGameNumber());
        gameBoardHardInternal.playMove(availableCells[index]);
        gameBoardHardInternal.setAvailableCells(availableCells[index]);

        int sum = 0;
        if (state.equals(botPlayer)) {
            return 10;
        } else if (state.equals("Draw")) {
            return 0;
        } else if (state.equals(anotherPlayer)) {
            return -10;
        } else {
            int scoreTally = 0;
            for (int i = 0; i < gameBoardHardInternal.getPlayableMoves(); i++) {
                scoreTally += miniMax(gameBoardMain, gameBoardHardInternal, i);
                gameBoardHardInternal.printGameBoard();
                gameBoardHardInternal = GameBoard.newInstance(gameBoard);
            }
            //System.out.println(scoreTally);
            gameBoardHardInternal = GameBoard.newInstance(gameBoardMain);
            return scoreTally;
        }

    }
    */

    public int bestMove(GameBoard gameBoard) {
        gameBoard.setState();
        String state = gameBoard.getState();
        //gameBoard.printGameBoard();
        if (state.equals(this.botPlayer)) {
            //System.out.println(this.botPlayer + " Wins");
            return 10 * (gameBoard.getPlayableMoves() + 1);
        } else if (state.equals("Draw")) {
            return 0;
        } else if (state.equals(this.anotherPlayer)) {
            //System.out.println(this.anotherPlayer + " Wins");
            return -10 * (gameBoard.getPlayableMoves() + 1);
        } else {
            int result = 0;
            GameBoard gameBoardOld = GameBoard.newInstance(gameBoard);

            for (int[] value : gameBoardOld.getAvailableCells()) {
                gameBoard.playMove(value);
                result += bestMove(gameBoard);
                //System.out.println(result);
                //gameBoard.printGameBoard();
                gameBoard = GameBoard.newInstance(gameBoardOld);
            }

            return result;
        }
    }

}
