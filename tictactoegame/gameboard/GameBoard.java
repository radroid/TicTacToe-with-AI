package tictactoegame.gameboard;

public class GameBoard {
    private String[][] currentBoard = new String[3][3];
    private int gameNumber = 0;
    private String state = "Not finished";
    private int[][] availableCells;
    private int playableMoves;

    public GameBoard() {
        this.availableCells = new int[9][2];
        for (int i = 0, k = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                this.currentBoard[i][j] = "_";
                this.availableCells[k][0] = i;
                this.availableCells[k][1] = j;
                k++;
            }
        } // Initialising the table with "_". To ensure easier processing.

        this.playableMoves = 9;
        this.gameNumber = 1;
        setState();
    }
    public GameBoard(GameBoard gameBoardOld) {
        this();
        this.gameNumber = gameBoardOld.getGameNumber() + 1;
    }

    public static GameBoard newInstance(GameBoard anotherGameBoard) {
        return new GameBoard(anotherGameBoard.getCurrentBoard(), anotherGameBoard.getAvailableCells());
    }

    public GameBoard(String[][] currentBoard, int[][] availableCells) {

        for (int i = 0; i < 3; i++) {
            this.currentBoard[i] = currentBoard[i].clone();
        }

        int len = availableCells.length;
        this.availableCells = new int[len][2];
        for (int k = 0; k < len; k++) {
            this.availableCells[k][0] = availableCells[k][0];
            this.availableCells[k][1] = availableCells[k][1];
        }
        this.playableMoves = this.availableCells.length;
    }

    public String[][] getCurrentBoard() {
        return this.currentBoard;
    }

    public void setCurrentBoard(String inputCells) {
        String[][] currentBoard = new String[3][3];
        int loc = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                currentBoard[i][j] = String.valueOf(inputCells.charAt(loc));
                loc++;
            }
        }
        this.currentBoard = currentBoard;
    } // Converts the user input into array.

    public int getGameNumber() {
        return gameNumber;
    }

    public void printGameBoard() {
        System.out.println("  | 1 2 3 |");
        System.out.println("-------------");
        for (int i = 0, k = 3; i < 3; i++) {
            System.out.printf("%d | ", k--);
            for (int j = 0; j < 3; j++) {
                System.out.print(this.currentBoard[i][j].equals("_") ? "  " : this.currentBoard[i][j] + " ");
            }
            System.out.println("|");
        }
        System.out.println("-------------");
    } // Prints the current table/position of the game.

    public void playMove(int[] coordinates) {

        int[] numXO = countXnOs();
        int xNum = numXO[0];
        int oNum = numXO[1];

        int rowNum = coordinates[0];
        int colNum = coordinates[1];

        this.currentBoard[rowNum][colNum] = xNum <= oNum ? "X" : "O";
        setAvailableCells(coordinates);
    }

    public void deleteMove(int[] coordinates) {
        int rowNum = coordinates[0];
        int colNum = coordinates[1];
        this.currentBoard[rowNum][colNum] = "_";
    }

    public void playMove(int[] coordinates, String player) {

        int rowNum = coordinates[0];
        int colNum = coordinates[1];

        this.currentBoard[rowNum][colNum] = player;

        setState();
    } // For Medium level bot to test

    public int[] countXnOs() {
        int[] result = new int[2];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                switch (this.currentBoard[i][j]) {
                    case "X":
                        result[0]++;
                        break;
                    case "O":
                        result[1]++;
                        break;
                    default:
                        break;
                }
            }
        }
        return result;
    } //calculates the number of Xs and Os.

    public void setState() {
        boolean win = false;
        boolean gameFinished;
        String[][] cP = this.currentBoard;
        String cellCent = cP[1][1];

        int[][] cases = new int[4][4];
        cases[0] = new int[]{0, 0, 2, 2};
        cases[1] = new int[]{0, 2, 2, 0};
        cases[2] = new int[]{0, 1, 2, 1};
        cases[3] = new int[]{1, 0, 1, 2};

        for (int i = 0; i < 4; i++) {
            if (!cellCent.equals("_")) {
                win = cellCent.equals(cP[cases[i][0]][cases[i][1]]) &&
                        cellCent.equals(cP[cases[i][2]][cases[i][3]]);
            }
            if (win) {
                this.state = cellCent;
                return;
            }
        } //checks using the center cell.

        cases[0] = new int[]{0, 2, 0, 0};
        cases[1] = new int[]{2, 0, 0, 0};
        cases[2] = new int[]{2, 0, 2, 2};
        cases[3] = new int[]{0, 2, 2, 2};

        int[][] cents = new int[4][2];
        cents[0] = new int[]{0, 1};
        cents[1] = new int[]{1, 0};
        cents[2] = new int[]{2, 1};
        cents[3] = new int[]{1, 2};

        for (int i =0; i < 4; i++) {
            if (!cP[cents[i][0]][cents[i][1]].equals("_")) {
                win = cP[cents[i][0]][cents[i][1]].equals(cP[cases[i][0]][cases[i][1]]) &&
                        cP[cents[i][0]][cents[i][1]].equals(cP[cases[i][2]][cases[i][3]]);
            }
            if (win) {
                this.state = cP[cents[i][0]][cents[i][1]];
                return;
            }
        } /* checking the edges. The process takes the centers of the edges and compares it
        to the adjust cells. Hence, cents represent the coordinates of the central cells and
        cases the coordinates of the adjacent */

        if (this.playableMoves != 0) {

            return;
        }

        this.state = "Draw";
    } // updates the status of the game and prints it accordingly.

    public String getState() {
        return state;
    }

    public void printState() {
        if ("Draw".equals(this.state)) {
            System.out.println(this.state);
        } else if ("Not finished".equals(this.state)) {
            System.out.println("The game is not finished");
        } else {
            System.out.println(this.state + " wins");
        }
    }

    public void setAvailableCells(int[] coordinates) {

        if (this.playableMoves - 1 <= 0) {
            this.availableCells = null;
            decrePlayableMoves();
            setState();
            return;
        }

        int[][] arr = new int[this.playableMoves - 1][2];

        for (int i = 0, k = 0; i < this.playableMoves; i++) {
            if (this.availableCells[i][0] == coordinates[0] && this.availableCells[i][1] == coordinates[1]) {
                continue;
                //skip the played move and replace for the played move, but in the last row.
            }

            arr[k++]= this.availableCells[i].clone();

        } //modifying the array of available cells.

        this.availableCells = arr.clone();
        decrePlayableMoves();
        setState();
    }

    public int getPlayableMoves() {
        return playableMoves;
    }

    public void decrePlayableMoves() {
        this.playableMoves--;;
    }

    public int[][] getAvailableCells() {
        return availableCells;
    }
}
