package tictactoe;

import hardbot.HardBot;
import mediumbot.MediumBot;

import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        HardBot hardBot1 = new HardBot("X", "O");
        MediumBot mediumBot = new MediumBot();
        HardBot hardBot2 = new HardBot("O", "X");
        GameBoard gameBoard1 = new GameBoard();
        GameBoard gameBoard2 = new GameBoard();
        GameBoard gameBoard3 = GameBoard.newInstance(gameBoard2);

        /*

        gameBoard2.playMove(gameBoard2.getAvailableCells()[0]);
        gameBoard2.playMove(gameBoard2.getAvailableCells()[0]);
        gameBoard2.playMove(gameBoard2.getAvailableCells()[1]);
        gameBoard2.playMove(gameBoard2.getAvailableCells()[2]);

        */

        gameBoard2.printGameBoard();

        gameBoard2.playMove(hardBot1.generateBestCoordinates(gameBoard2));
        gameBoard2.playMove(hardBot2.generateBestCoordinates(gameBoard2));



        gameBoard1.printGameBoard();
        gameBoard2.printGameBoard();
        gameBoard3.printGameBoard();



    }
}
