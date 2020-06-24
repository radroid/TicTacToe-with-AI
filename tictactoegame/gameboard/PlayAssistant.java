package tictactoegame.gameboard;

import tictactoegame.DBMS;
import tictactoegame.bots.EasyBot;
import tictactoegame.bots.HardBot;
import tictactoegame.bots.MediumBot;

import java.util.Scanner;

class PlayAssistant {


    private String PlayerOne;
    private String PlayerTwo;
    private int gamesToBePlayed;
    private int gamesPlayed;
    private int drawGames;

    // Main menu commands
    public void playNewGame() {

        subMenuOne();
        if ("none".equalsIgnoreCase(getPlayerOne())) {
            System.out.println("Thank you for opening the game! See you later.");
            return;
        }

        // Create objects of each player
        Players playerOne = createPlayers(getPlayerOne(), true);
        Players playerTwo = createPlayers(getPlayerTwo(), false);

        boolean endGame = false;
        Scanner scanner = new Scanner(System.in);
        GameBoard gameBoard = new GameBoard();

        // Same game loop: stay while player wants to play with same settings
        while (!endGame) {

            assert playerOne != null;
            assert playerTwo != null;
            String currentPlayer = playerOne.getPlayerName();
            int[] moveCoordinates = new int[2];

            System.out.printf("%nGame %d begins...%n%n", gameBoard.getGameNumber());
            gameBoard.printGameBoard();

            // Instructions for inputting coordinates
            if (getGamesPlayed() <= 2) {
                System.out.println("Remember, you have to enter the coordinates of your next move with a space between them.");
            }

            // Main gameplay
            while ("Not finished".equals(gameBoard.getState())) {

                // For switching turns and getting coordinates
                if (playerOne.getPlayerName().equals(currentPlayer)) {
                    moveCoordinates = playerOne.getCoordinates(gameBoard);
                    currentPlayer = playerTwo.getPlayerName();
                } else if (playerTwo.getPlayerName().equals(currentPlayer)) {
                    moveCoordinates = playerTwo.getCoordinates(gameBoard);
                    currentPlayer = playerOne.getPlayerName();
                }

                gameBoard.playMove(moveCoordinates);
                gameBoard.printGameBoard();
            }

            gameBoard.printState(); //Print result

            // Update records
            updateRecords(gameBoard.getState(), playerOne, playerTwo);

            // Print full scoreboard
            printScoreBoard();

            // Play another round
            if (getGamesToBePlayed() == 1 || getGamesPlayed() >= getGamesToBePlayed()) {
                System.out.println("Would you like to make one more game? Y/N");
                endGame = "N".equalsIgnoreCase(scanner.next().trim());
            }

            // Resetting or Initialising 'Gameboard' Object
            if (!endGame) {
                gameBoard = new GameBoard(gameBoard.getGameNumber());
            }
        }
    }
    public void printScoreBoard() {
        DBMS dbms = new DBMS();
        dbms.printScores();
    }
    public void about() {
        Scanner scan = new Scanner(System.in);
        System.out.printf("Hi! I am your Play Assistant.%n" +
                "I am here for you outside the game. Okay, so let me tell you how this works.%n%n" +
                "You have this concise main menu with all the available options. " +
                "Start a new game and see more options.%n" +
                "- Easy bot: makes a random move and wins if possible.%n" +
                "- Medium bot: blocks and wins if possible.%n" +
                "- Hard (undefeatable) bot: It will be a draw if you play well ;D%n" +
                "%nRules are simple. It is tic tac toe, play to win, you know that. " +
                "If you do not know the rules google: 'tic tac toe rules'%n" +
                "%nA little something about me:%n" +
                "I am a Mechanical (Nuclear) Engineer, who decided to switch careers and am now working towards " +
                "becoming a Machine Learning Engineer. This is my first big independent project. " +
                "I have been refactoring and updating the code every time I get an idea that could make this better. " +
                "A perfect example of one these instances would be: implementation of a database with SQLite. " +
                "For more information on the updates to the project please visit my Github profile.%n" +
                "%nIf you want to know more about me, visit: github.com/Raj-007%n" +
                "%nPlease enter any character to go: ");

        scan.next();

    }

    // Methods to assist Main Menu Commands
    private void subMenuOne() {
        Scanner scan = new Scanner(System.in);

        System.out.print("\n\n1. Human vs. Computer\n" +
                "2. Human vs. Human\n" +
                "3. Computer vs. Computer\n" +
                "0. Exit\n");

        switch (getValidInput(0, 3)) {
            case 1 -> {
                System.out.print("\n\n1. Advanced settings\n" +
                        "2. Standard settings\n" +
                        "\t - Player one = Human\n" +
                        "\t - Computer difficulty level = Medium\n" +
                        "\t - Game to be played: 'Best of 1'\n" +
                        "0. Exit\n");

                switch (getValidInput(0, 2)) {
                    case 1 -> {

                        // Select who plays first
                        System.out.println("Is player one to be played by the computer? " +
                                "*The computer will play first\n" +
                                "(Y/N)");
                        boolean isPlayerOneComp = "Y".equalsIgnoreCase(scan.next().trim());

                        // Select the number of games
                        System.out.println("Select the number of games to be played in the same mode:\n" +
                                "1. Best of 3\n" +
                                "2. Best of 5\n" +
                                "3. Custom\n");
                        switch (getValidInput(1, 3)) {
                            case 1 -> setGamesToBePlayed(3);
                            case 2 -> setGamesToBePlayed(5);
                            case 3 -> {
                                System.out.println("Enter the number of games:\n > ");
                                setGamesToBePlayed(scan.nextInt());
                            }
                        }

                        // Select the difficult level
                        System.out.println("Select the computer difficulty level:\n" +
                                "1. Easy\n" +
                                "2. Medium\n" +
                                "3. Hard\n");
                        if (isPlayerOneComp) {
                            switch (getValidInput(1, 3)) {
                                case 1 -> setPlayerOne("easy");
                                case 2 -> setPlayerOne("medium");
                                case 3 -> setPlayerOne("hard");
                            }
                            setPlayerTwo("user");
                        } else {
                            switch (getValidInput(1, 3)) {
                                case 1 -> setPlayerTwo("easy");
                                case 2 -> setPlayerTwo("medium");
                                case 3 -> setPlayerTwo("hard");
                            }
                            setPlayerOne("user");
                        }

                    }
                    case 2 -> {
                        setPlayerOne("User");
                        setPlayerTwo("Medium");
                        setGamesToBePlayed(1);
                    }
                    case 0 -> setPlayerOne("none");
                }

            }
            case 2 -> {
                setPlayerOne("user");
                setPlayerTwo("user");
            }
            case 3 -> {
                System.out.print("\n\n1. Advanced settings\n" +
                        "2. Standard settings\n" +
                        "\t - Player one = Easy computer\n" +
                        "\t - Player two = Medium computer\n" +
                        "\t - Game to be played: 'Best of 1'\n" +
                        "0. Exit\n");

                switch (getValidInput(0, 2)) {
                    case 1 -> {

                        // Select the number of games
                        System.out.println("Select the number of games to be played in the same mode:\n" +
                                "1. Best of 3\n" +
                                "2. Best of 5\n" +
                                "3. Custom\n");
                        switch (getValidInput(1, 3)) {
                            case 1 -> setGamesToBePlayed(3);
                            case 2 -> setGamesToBePlayed(5);
                            case 3 -> {
                                System.out.println("Enter the number of games:\n > ");
                                setGamesToBePlayed(scan.nextInt());
                            }
                        }

                        // Select the difficult level
                        System.out.println("Select the computer difficulty level:\n" +
                                "1. Easy\n" +
                                "2. Medium\n" +
                                "3. Hard\n");

                        System.out.println("Set computer one:\n");
                        switch (getValidInput(1, 3)) {
                            case 1 -> setPlayerOne("easy");
                            case 2 -> setPlayerOne("medium");
                            case 3 -> setPlayerOne("hard");
                        }

                        System.out.println("Set computer two:\n");
                        switch (getValidInput(1, 3)) {
                            case 1 -> setPlayerTwo("easy");
                            case 2 -> setPlayerTwo("medium");
                            case 3 -> setPlayerTwo("hard");
                        }
                    }
                    case 2 -> {
                        setPlayerOne("easy");
                        setPlayerTwo("medium");
                        setGamesToBePlayed(1);
                    }
                    case 0 -> setPlayerOne("none");
                }
            }
            case 0 -> setPlayerOne("none");
        }

    }
    private int getValidInput(int minOption, int maxOption) {
        Scanner scan = new Scanner(System.in);
        boolean isValid = false;
        int number = 0;

        do {
            System.out.print("\n > ");
            String input = scan.next();
            if (input.length() > 1 || !Character.isDigit(input.charAt(0))) {
                System.out.println("Please enter a valid response. Enter a number corresponding to the options provided.\n");
                continue;
            }
            number = Integer.parseInt(input);
            isValid = number >= minOption && number <= maxOption;
        } while (!isValid);

        return number;
    }
    private Players createPlayers(String playerType, boolean isPlayerOne) {
        Scanner scanner = new Scanner(System.in);
        if (isPlayerOne) {
            switch (playerType) {
                case "user" -> {
                    System.out.print("Player One, please enter your name: ");
                    String playerOneName = scanner.nextLine();
                    return new User(playerOneName);
                }
                case "easy" -> {
                    return new EasyBot("Easy Bot Player One");
                }
                case "medium" -> {
                    return new MediumBot("Medium Bot Player One");
                }
                case "hard" -> {
                    return new HardBot("Hard Bot Player One", "X", "O");
                }
            } //creating player one
        } else {
            switch (playerType) {
                case "user" -> {
                    System.out.print("Player Two, please enter your name: ");
                    String playerOneName = scanner.nextLine();
                    return new User(playerOneName);
                }
                case "easy" -> {
                    return new EasyBot("Easy Bot Player Two");
                }
                case "medium" -> {
                    return new MediumBot("Medium Bot Player Two");
                }
                case "hard" -> {
                    return new HardBot("Hard Bot Player Two", "O", "X");
                }
            } //creating player two
        }
        return null;
    }
    private void updateRecords(String result, Players playerOne, Players playerTwo) {

        // Increase number of games played
        setGamesPlayed(getGamesPlayed() + 1);

        switch (result) {
            case "X" -> {
                playerOne.recordResult("win");
                playerTwo.recordResult("loss");
            }
            case "O" -> {
                playerOne.recordResult("loss");
                playerTwo.recordResult("win");
            }
            default -> {
                setDrawGames(getDrawGames() + 1);
                playerOne.recordResult("draw");
                playerTwo.recordResult("draw");
            }
        }

    }

    // Setters and Getters

    public void setPlayerOne(String playerOne) {
        PlayerOne = playerOne;
    }
    public String getPlayerOne() {
        return PlayerOne;
    }

    public void setPlayerTwo(String playerTwo) {
        PlayerTwo = playerTwo;
    }
    public String getPlayerTwo() {
        return PlayerTwo;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }
    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesToBePlayed(int gamesToBePlayed) {
        this.gamesToBePlayed = gamesToBePlayed;
    }
    public int getGamesToBePlayed() {
        return gamesToBePlayed;
    }

    public void setDrawGames(int drawGames) {
        this.drawGames = drawGames;
    }
    public int getDrawGames() {
        return drawGames;
    }
}
