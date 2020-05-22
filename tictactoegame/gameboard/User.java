package tictactoegame.gameboard;

import java.util.Scanner;

class User extends Players {

    private String playerName;
    private int wins = 0;

    public User(String playerName) {
        setPlayerName(playerName);
    }
    public String getPlayerName() {
        return playerName;
    }
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    public int getWins() {
        return wins;
    }
    public void recordWin() {
        this.wins++;
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

            if (len == 1 || input.isEmpty() || len > 2) {
                System.out.println("Please enter only two numbers. Look at the coordinate system defined on the board.");
                continue;
            } // check if more than two variables are input.

            if (input.trim().charAt(0) >= '0' && input.trim().charAt(0) <= '9') {
                result[0] = Integer.parseInt(inputMat[0]);
                end1 = true;
            } else {
                System.out.println("Enter only numerical values.");
                continue;
            } // check if first character of input is a number

            var charAtEnd = input.trim().charAt(input.length() - 1);

            if (charAtEnd >= '0' && charAtEnd <= '9') {
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
