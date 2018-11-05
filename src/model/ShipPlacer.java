package model;

import model.board.Board;
import model.board.BoardFieldsPermittedException;
import model.board.OutOfBoardException;

import java.util.Random;

public class ShipPlacer {
    private Board board;
    private int shipSize1Count;
    private int shipSize2Count;
    private int shipSize3Count;
    private int shipSize4Count;
    private Ship[] ships;
    private Random random = new Random();

    public ShipPlacer(Board board, int size1Count, int size2Count, int size3Count, int size4Count)
    {
        this.board = board;
        shipSize1Count = size1Count;
        shipSize2Count = size2Count;
        shipSize3Count = size3Count;
        shipSize4Count = size4Count;
        ships = new Ship[size1Count + size2Count + size3Count + size4Count];

        createShips();
    }

    private void createShips()
    {
        int index = 0;

        for (int i = 0; i < shipSize4Count; i++, index++) {
            ships[index] = new Ship(4);
        }
        for (int i = 0; i < shipSize3Count; i++, index++) {
            ships[index] = new Ship(3);
        }
        for (int i = 0; i < shipSize2Count; i++, index++) {
            ships[index] = new Ship(2);
        }
        for (int i = 0; i < shipSize1Count; i++, index++) {
            ships[index] = new Ship(1);
        }
    }

    public void placeShips()
    {
        for (Ship ship: ships) {
            boolean shipPlaced = false;

            while (!shipPlaced) {
                int x = random.nextInt(board.getSize() - 1);
                int y = random.nextInt(board.getSize() - 1);

                if (random.nextBoolean()) {
                    ship.changeOrientation();
                }

                try {
                    board.addShip(ship, x, y);
                    shipPlaced = true;
                } catch (OutOfBoardException | BoardFieldsPermittedException exception) {
                }
            }
        }
    }
}
