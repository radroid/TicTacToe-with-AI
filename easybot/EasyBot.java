package easybot;

import java.util.Random;

public class EasyBot {
    public int[] generateCoordinates(String[][] curTable) {
        Random random = new Random();
        int[] coordinates = new int[2];
        boolean available = false;
        int rowNum = 0, colNum = 0;

        while (!available) {
            rowNum = random.nextInt(3);
            colNum = random.nextInt(3);
            available = curTable[rowNum][colNum].equals("_");
        }
        coordinates[0] = rowNum;
        coordinates[1] = colNum;
        return coordinates;
    } // selects a random set of available coordinates and returns.
}
