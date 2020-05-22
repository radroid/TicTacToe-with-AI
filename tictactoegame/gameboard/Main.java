package tictactoegame.gameboard;

import tictactoegame.bots.EasyBot;
import tictactoegame.bots.MediumBot;
import tictactoegame.bots.HardBot;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        PlayAssistant playAssistant = new PlayAssistant();
        playAssistant.startGame();
        boolean endGame = false;

        if (!"end".equals(playAssistant.getInputCommand())) {
            Players playerOne = null;
            switch (playAssistant.getPlayerOne()) {
                case "user" -> {
                    System.out.print("Player One, please enter your name: ");
                    String playerOneName = scanner.nextLine();
                    playerOne = new User(playerOneName);
                }
                case "easy" -> playerOne = new EasyBot("Easy Bot Player One");
                case "medium" -> playerOne = new MediumBot("Medium Bot Player One");
                case "hard" -> playerOne = new HardBot("Hard Bot Player One", "X", "O");
            } //creating player one

            Players playerTwo = null;
            switch (playAssistant.getPlayerTwo()) {
                case "user" -> {
                    System.out.print("Player Two, please enter your name: ");
                    String playerOneName = scanner.nextLine();
                    playerTwo = new User(playerOneName);
                }
                case "easy" -> playerTwo = new EasyBot("Easy Bot Player Two");
                case "medium" -> playerTwo = new MediumBot("Medium Bot Player Two");
                case "hard" -> playerTwo = new HardBot("Hard Bot Player Two", "O", "X");
            } //creating player two

            GameBoard gameBoard = new GameBoard();

            while (!endGame) {

                System.out.printf("%nGame %d begins...%n%n", gameBoard.getGameNumber());
                gameBoard.printGameBoard();

                assert playerOne != null;
                assert playerTwo != null;
                String player = playerOne.getPlayerName();

                if (playAssistant.getGamesPlayed() <= 3) {
                    System.out.println("Remember, you have to enter the coordinates of your next move with a space between them.");
                }

                int[] moveCoordinates = new int[2];

                while ("Not finished".equals(gameBoard.getState())) {

                    if (playerOne.getPlayerName().equals(player)) {
                        moveCoordinates = playerOne.getCoordinates(gameBoard);
                        player = playerTwo.getPlayerName();
                    } else if (playerTwo.getPlayerName().equals(player)) {
                        moveCoordinates = playerTwo.getCoordinates(gameBoard);
                        player = playerOne.getPlayerName();
                    } //alternates between players

                    gameBoard.playMove(moveCoordinates);
                    gameBoard.printGameBoard();
                }
                gameBoard.printState();

                playAssistant.setGamesPlayed(playAssistant.getGamesPlayed() + 1);

                if ("X".equals(gameBoard.getState())) {
                    playerOne.recordWin();
                } else if ("O".equals(gameBoard.getState())){
                    playerTwo.recordWin();
                } else {
                    playAssistant.setDrawGames(playAssistant.getDrawGames() + 1);
                }

                playAssistant.printScores(playerOne, playerTwo);

                if (playAssistant.getGamesToBePlayed() == 1 || playAssistant.getGamesPlayed() >= playAssistant.getGamesToBePlayed()) {
                    System.out.println("Would you like to make one more game? Y/N");
                    endGame = "N".equalsIgnoreCase(scanner.next().trim());
                }

                if (!endGame) {
                    gameBoard = new GameBoard(gameBoard);
                }
            }
        }
    }
}

class PlayAssistant {

    private String inputCommand;
    private String PlayerOne;
    private String PlayerTwo;
    private int gamesToBePlayed;
    private int gamesPlayed;
    private int drawGames;

    public void startGame() {
        Scanner scan = new Scanner(System.in);

        System.out.printf("Hi! I am your Play Assistant.%n" +
                "I am here for you outside the game. Okay, so let me tell you how this works.%n" +
                "you can play multiple games against a particular player. It can be any of the following players playing against each other (even itself!).%n" +
                "- Easy bot: makes a random move.%n" +
                "- Medium bot: blocks / wins if possible.%n" +
                "- Hard (undefeatable) bot: It will be a draw if you are good ;D%n" +
                "- and, of course, you! The User.%n" +
                "%nRules are simple. It is tic tac toe, play to win, know that %n" +
                "%nSo then, let's start!%n" +
                "%nPlease press enter any character to continue or type \"end\" to quit.%n");
        String startGame = scan.next();
        if ("end".equals(startGame)) {
            System.out.println("Thank you for opening the game! See you later.");
            this.inputCommand = startGame;
            return;
        }

        gameSetup();
        setPlayerTypes();
    }

    protected void setPlayerTypes() {
        System.out.printf("Let's start with who should be the player will be (enter your choice of type):%n" +
                "- \"User\": type this if you want to play.%n" +
                "- \"Easy\"%n" +
                "- \"Medium\"%n" +
                "- \"Hard\"%n" +
                "Player One type: ");
        setPlayerOne(validInput());
        System.out.print("Player Two type: ");
        setPlayerTwo(validInput());
    }

    protected String validInput() {
        Scanner scanner = new Scanner(System.in);
        String player = scanner.next().trim();

        if (!"user".equalsIgnoreCase(player) && !"easy".equalsIgnoreCase(player) && !"medium".equalsIgnoreCase(player) && !"hard".equalsIgnoreCase(player)) {
            System.out.println("Please enter a valid player type.");
            System.out.print("Re-enter type: ");
            return validInput();
        }
        return player.toLowerCase();
    }

    protected void gameSetup() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Use advanced setting to setup game? Y/N");
        boolean advancedGame = "Y".equalsIgnoreCase(scanner.next().trim());

        setGamesPlayed(0);
        setDrawGames(0);

        if (!advancedGame) {
            setGamesToBePlayed(1);
            return;
        }

        System.out.printf("You have entered a slightly advanced mode now.%n" +
                "Currently, all you can you do here is set the number of games you would like to play.%n" +
                "So please enter the number of games (best of...): ");
        setGamesToBePlayed(scanner.nextInt());
    }

    public void printScores(Players playerOne, Players playerTwo) {
        boolean isPlayer1Longer = playerOne.getPlayerName().length() > playerTwo.getPlayerName().length();
        int len = isPlayer1Longer ? playerOne.getPlayerName().length() : playerTwo.getPlayerName().length();
        int lenMod = Math.abs(playerOne.getPlayerName().length() - playerTwo.getPlayerName().length());
        for (int i = 0; i < len; i++) {
            System.out.print(" ");
        }
        System.out.printf("\tWin\tDraws%n");

        if (isPlayer1Longer) {
            System.out.printf("%s\t%d\t%d%n", playerOne.getPlayerName(), playerOne.getWins(), getDrawGames());
            System.out.printf("%s", playerTwo.getPlayerName());
            for (int i = 0; i < lenMod; i++) {
                System.out.print(" ");
            }
        System.out.printf("\t%d\t%d%n%n", playerTwo.getWins(), getDrawGames());
        } else {
            System.out.printf("%s", playerOne.getPlayerName());
            for (int i = 0; i < lenMod; i++) {
                System.out.print(" ");
            }
            System.out.printf("\t%d\t%d%n", playerOne.getWins(), getDrawGames());
            System.out.printf("%s\t%d\t%d%n%n", playerTwo.getPlayerName(), playerTwo.getWins(), getDrawGames());
        }
    }

    // Setters and Getters

    public String getInputCommand() {
        return inputCommand;
    }

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