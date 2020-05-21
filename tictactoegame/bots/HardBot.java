package tictactoegame.bots;

import tictactoegame.gameboard.GameBoard;
import tictactoegame.gameboard.Players;

public class HardBot extends Players {

    String botPlayer;
    String anotherPlayer;
    String playerName;
    int botWins;

    public HardBot(String botName, String botPlayer, String anotherPlayer) {
        setBotPlayer(botPlayer);
        setAnotherPlayer(anotherPlayer);
        setPlayerName(botName);
    }

    public int getWins() {
        return botWins;
    }
    public void recordWin() {
        this.botWins++;
    }

    public void setPlayerName(String botName) {
        this.playerName = botName;
    }
    public String getPlayerName() {
        return playerName;
    }

    public void setAnotherPlayer(String anotherPlayer) {
        this.anotherPlayer = anotherPlayer;
    }
    public void setBotPlayer(String botPlayer) {this.botPlayer = botPlayer;
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
        
        int[] scoreTally = new int[gameBoard.getPlayableMoves()];
        GameBoard gameBoard1 = GameBoard.newInstance(gameBoard);

        for (int i = 0; i < gameBoard.getPlayableMoves(); i++) {
            gameBoard1.playMove(gameBoard1.getAvailableCells()[i]);
            scoreTally[i] = bestMove(gameBoard1);
            gameBoard1 = GameBoard.newInstance(gameBoard);
        } // loop to get a score for each possible move.

        int result = 0;

        for (int i = 0; i < scoreTally.length; i++) {
            if (scoreTally[i] > scoreTally[result]) {
                result = i;
            }
        } // Finding the highest score for each possible move.

        return gameBoard.getAvailableCells()[result];
    }

    public int bestMove(GameBoard gameBoard) {
        gameBoard.setState();
        String state = gameBoard.getState();
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
                gameBoard = GameBoard.newInstance(gameBoardOld);
            }

            return result;
        }
    }

}
