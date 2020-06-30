package tictactoegame.bots;

import tictactoegame.DBMS;
import tictactoegame.gameboard.GameBoard;
import tictactoegame.gameboard.Players;

import java.util.Random;

public class MediumBot extends Players {

    String playerName;
    private int botWins;
    private int botDraws;
    private int botLosses;

    public MediumBot(String botName) {
        DBMS dbms = new DBMS();
        setPlayerName(botName);
        String player = "\"" + getPlayerName() + "\"";
        if (dbms.playerExists(botName)) {
            setBotWins(dbms.getData(player, "Wins"));
            setBotDraws(dbms.getData(player, "Draws"));
            setBotLosses(dbms.getData(player, "Losses"));
        } else {
            dbms.newPlayer(botName);
        }
    }

    public void setPlayerName(String botName) {
        this.playerName = botName;
    }
    public String getPlayerName() {
        return playerName;
    }

    public void setBotWins(int botWins) {
        this.botWins = botWins;
    }
    public void setBotDraws(int botDraws) {
        this.botDraws = botDraws;
    }
    public void setBotLosses(int botLosses) {
        this.botLosses = botLosses;
    }

    public int getWins() {
        return botWins;
    }
    public void recordResult(String result) {
        DBMS dbms = new DBMS();
        String player = "\"" + getPlayerName() + "\"";
        switch (result) {
            case "win" -> {
                this.botWins++;
                dbms.updateScore(player, "Wins");
            }
            case "draw" -> {
                this.botDraws++;
                dbms.updateScore(player, "Draws");
            }
            case "loss" -> {
                this.botLosses++;
                dbms.updateScore(player, "Losses");
            }
        }
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
                return availableCells[i];
            } else if ("Draw".equals(state)) {
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
