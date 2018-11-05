package model;

import model.board.Board;
import model.board.BoardFieldsPermittedException;
import model.board.OutOfBoardException;

import java.util.ArrayList;
import java.util.Random;

public class ShipPlacer {
    private Board board;
    private Random random = new Random();

    public ShipPlacer(Board board)
    {
        this.board = board;
    }

    public void placeShips(ArrayList<Ship> ships)
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
