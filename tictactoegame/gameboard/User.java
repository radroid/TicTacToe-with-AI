package tictactoegame.gameboard;

import tictactoegame.DBMS;

import java.util.Scanner;

class User extends Players {

    private String playerName;
    private int wins;
    private int draws;
    private int losses;

    public User(String playerName) {
        setPlayerName(playerName);
        DBMS dbms = new DBMS();
        if (dbms.playerExists(playerName)) {
            setWins(dbms.getData(playerName, "Wins"));
            setDraws(dbms.getData(playerName, "Draws"));
            setLosses(dbms.getData(playerName, "Losses"));
        } else {
            dbms.newPlayer(playerName);
        }
    }
    public String getPlayerName() {
        return playerName;
    }
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }
    public void setDraws(int draws) {
        this.draws = draws;
    }
    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getWins() {
        return wins;
    }
    public int getDraws() {
        return draws;
    }
    public int getLosses() {
        return losses;
    }

    public void recordResult(String result) {
        DBMS dbms = new DBMS();
        String player = "\"" + getPlayerName() + "\"";
        switch (result) {
            case "win" -> {
                this.wins++;
                dbms.updateScore(player, "Wins");
            }
            case "draw" -> {
                this.draws++;
                dbms.updateScore(player, "Draws");
            }
            case "loss" -> {
                this.losses++;
                dbms.updateScore(player, "Losses");
            }
        }
    }

    public int[] getCoordinates(GameBoard gameBoard) {
        Scanner scan = new Scanner(System.in);
        int[] result = new int[2];

        String input;
        String[] inputMat;
        int len;
        boolean end1 = false, end2 = false;
        int colNum;
        int rowNum;
        boolean available = false;

        System.out.println("Enter column number (horizontal location) first and row number (vertical) second.");

        while (!end1 || !end2 || !available) {

            System.out.print("Enter the coordinates of your move: ");
            input = scan.nextLine();

            inputMat = input.trim().split(" ");

            len = inputMat.length;

            // check if more than two variables are input.
            if (len == 1 || input.isEmpty() || len > 2) {
                System.out.println("Please enter only two numbers. Look at the coordinate system defined on the board.");
                continue;
            }

            // check if first character of input is a number
            if (Character.isDigit(input.trim().charAt(0))) {
                result[0] = Integer.parseInt(inputMat[0]);
                end1 = true;
            } else {
                System.out.println("Enter only numerical values.");
                continue;
            }

            char charAtEnd = input.trim().charAt(input.trim().length() - 1);

            if (Character.isDigit(charAtEnd)) {
                result[1] = Integer.parseInt(inputMat[1]);
                end2 = true;
            } else {
                System.out.println("Enter only numerical values.");
                continue;
            } // Check if last character of input is a number.

            colNum = result[0] - 1;
            rowNum = 3 - result[1];

            if (colNum > 2 || colNum < 0 || rowNum > 2 || rowNum < 0) {
                System.out.println("Coordinates should be from 1 to 3!");
                available = false;
                continue;
            } // Check for coordinates within range.

            available = gameBoard.getCurrentBoard()[rowNum][colNum].equals("_"); // Check if the cell is available to play move.

            if (!available) {
                System.out.println("Oops, this cell is occupied! Please choose another one.");
                continue;
            } // loop again if not available.

            result[0] = rowNum;
            result[1] = colNum;
        }
        return result;
    } // gets a valid input for coordinates from the user.

}
