package tictactoegame.gameboard;

public class Main {
    public static void main(String[] args) {

        //Object creation and variable definition
        PlayAssistant playAssistant = new PlayAssistant();
        boolean close = false;

        // looping the Main menu till user exits.
        while (!close) {
            System.out.print("TIC TAC TOE - MAIN MENU\n" +
                    "1. Start New Game\n" +
                    "2. Scoreboard\n" +
                    "3. About\n" +
                    "0. Exit\n");
            switch (playAssistant.getValidInput(0, 3)) {
                case 1 -> playAssistant.playNewGame();
                case 2 -> playAssistant.printScoreBoard();
                case 3 -> playAssistant.about();
                case 0 -> {
                    playAssistant.exit();
                    close = true;
                }
            }
            if ("none".equalsIgnoreCase(playAssistant.getPlayerOne())) {
                break;
            }
        }
    }
}

