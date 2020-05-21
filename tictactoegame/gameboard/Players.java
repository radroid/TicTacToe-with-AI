package tictactoegame.gameboard;

public abstract class Players {

    public abstract int[] getCoordinates(GameBoard gameBoard);

    public abstract String getPlayerName();

    public abstract int getWins();

    public abstract void recordWin();

}
