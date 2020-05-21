package tictactoe;

import easybot.EasyBot;
import mediumbot.MediumBot;
import hardbot.HardBot;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        PlayAssistant playAssistant = new PlayAssistant();
        playAssistant.setInputCommand();
        String[] inputCommand = playAssistant.getInputCommand();

        if ("start".equals(inputCommand[0])) {

            /*if ("user".equals(inputCommand[1])) {
                System.out.print("Player One, please enter your name: ");
                String playerOneName = scan.nextLine();


            } else if ("easy".equals(inputCommand[1])) {

            }

            if ("user".equals(inputCommand[1])) {
                /*System.out.print("Player One, please enter your name: ");
                String playerOneName = scan.nextLine();

                User userTwo = new User("Player Two");
            } else if ("easy".equals(inputCommand[1])) {
                EasyBot easyBot2 = new EasyBot();
            } */

            User userOne = new User("Player One");
            EasyBot easyBot = new EasyBot();
            MediumBot mediumBot = new MediumBot();
            HardBot hardBot = new HardBot("X", "O");

            String playerOne = inputCommand[1];
            String playerTwo = inputCommand[2];

            GameBoard gameBoard = new GameBoard();
            gameBoard.printGameBoard();

            String player = playerOne;
            int[] moveCoordinates = new int[2];

            while ("Not finished".equals(gameBoard.getState()) && "start".equals(inputCommand[0])) {

                switch (player) {
                    case "user":
                        moveCoordinates = userOne.getCoordinates(gameBoard.getCurrentBoard());
                        player = "user".equals(playerOne) ? playerTwo : playerOne;
                        break;
                    case "easy":
                        System.out.println("Making move level \"easy\" ");
                        moveCoordinates = easyBot.generateCoordinates(gameBoard.getCurrentBoard());
                        player = "easy".equals(playerOne) ? playerTwo : playerOne;
                        break;
                    case "medium":
                        System.out.println("Making move level \"medium\" ");
                        moveCoordinates = mediumBot.winCoordinates(gameBoard);
                        player = "medium".equals(playerOne) ? playerTwo : playerOne;
                        break;
                    case "hard":
                        System.out.println("Making move level \"hard\" ");
                        moveCoordinates = hardBot.generateBestCoordinates(gameBoard);
                        player = "hard".equals(playerOne) ? playerTwo : playerOne;
                        break;
                } //alternates between players

                //System.out.println(Arrays.toString(moveCoordinates));
                //System.out.println(gameBoard.getGameNumber());
                gameBoard.playMove(moveCoordinates);
                gameBoard.printGameBoard();
            }
            gameBoard.printState();
        }
    }
}

interface Player {

    int[] getCoordinates(String[][] curTable);

}

class User implements Player {

    private String playerName;
    private int wins = 0;

    public User(String playerName) {
        this.playerName = playerName;
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
    public void recordWins() {
        this.wins++;
    }

    public int[] getCoordinates(String[][] curTable) {
        Scanner scan = new Scanner(System.in);
        int[] result = new int[2];

        String input;
        String[] inputMat;
        int len;
        boolean end1 = false, end2 = false;
        int colNum;
        int rowNum = 0;
        boolean available = false;

        while (!end1 || !end2 || !available) {

            System.out.print("Enter the coordinates of your move: ");
            input = scan.nextLine();

            inputMat = input.trim().split(" ");

            len = inputMat.length;

            if (len == 1 || input.isEmpty() || len > 2) {
                System.out.println("You should enter numbers!");
                continue;
            } // check if more than two variables are input.

            if (input.trim().charAt(0) >= '0' && input.trim().charAt(0) <= '9') {
                result[0] = Integer.parseInt(inputMat[0]);
                end1 = true;
            } else {
                System.out.println("You should enter numbers!");
                continue;
            } // check if first character of input is a number

            var charAtEnd = input.trim().charAt(input.length() - 1);

            if (charAtEnd >= '0' && charAtEnd <= '9') {
                result[1] = Integer.parseInt(inputMat[1]);
                end2 = true;
            } else {
                System.out.println("You should enter numbers!");
                continue;
            } // Check if last character of input is a number.

            colNum = result[0] - 1;
            rowNum = 3 - result[1];

            if (colNum > 2 || colNum < 0 || rowNum > 2 || rowNum < 0) {
                System.out.println("Coordinates should be from 1 to 3!");
                available = false;
                continue;
            } // Check for coordinates within range.

            available = curTable[rowNum][colNum].equals("_");

            if (!available) {
                System.out.println("This cell is occupied! Choose another one!");
                continue;
            } // Check if the cell is available to play move.
            result[0] = rowNum;
            result[1] = colNum;
        }
        return result;
    } // gets a valid input for coordinates from the user.

}

class PlayAssistant {

    private String[] inputCommand = new String[3];

    public void setInputCommand() {
        Scanner scan = new Scanner(System.in);
        String[] inputProcessor;

        while (true) {
            System.out.print("Input Command: ");
            String input = scan.nextLine();
            if ("exit".equals(input.trim())) {
                this.inputCommand[0] = input;
                break;
            } else {

                inputProcessor = input.trim().split(" ");

                if (inputProcessor.length != 3) {
                    System.out.println("Bad parameters!");
                    continue;
                } else if (!inputProcessor[0].equals("start") && !inputProcessor[0].equals("exit")) {
                    System.out.println("Bad parameters!");
                    continue;
                } else if (!inputProcessor[1].equals("user") && !inputProcessor[1].equals("easy") && !inputProcessor[1].equals("medium") && !inputProcessor[1].equals("hard")) {
                    System.out.println("Bad parameters!");
                    continue;
                } else if (!inputProcessor[2].equals("user") && !inputProcessor[2].equals("easy") && !inputProcessor[2].equals("medium")  && !inputProcessor[2].equals("hard")) {
                    System.out.println("Bad parameters!");
                    continue;
                }
            }
            this.inputCommand = inputProcessor;
            break;
        }
    }

    public String[] getInputCommand() {
        return inputCommand;
    }
}