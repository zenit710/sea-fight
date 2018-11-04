package model.board;

import model.Ship;

public class Board {
    private int size = 0;
    private Ship[][] ships;
    private boolean[][] availableFields;

    public Board(int size)
    {
        this.size = size;
        clearBoard();
    }

    public void addShip(Ship ship, int x, int y) throws OutOfBoardException, BoardFieldsPermittedException
    {
        if (requestedFieldsOutOfBoard(ship, x, y)) {
            throw new OutOfBoardException("Ship out of board");
        }

        if (!requestedFieldsFree(ship, x, y)) {
            throw new BoardFieldsPermittedException("Fields are not available");
        }

        placeShip(ship, x, y);
        permitFields(ship, x, y);
    }

    public Ship shoot(int x, int y)
    {
        if (ships[x][y] != null) {
            Ship ship = ships[x][y];
            ship.hit();

            return ship;
        }

        return null;
    }

    private void clearBoard()
    {
        ships = new Ship[size][size];
        availableFields = new boolean[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                ships[i][j] = null;
                availableFields[i][j] = true;
            }
        }
    }

    private void permitFields(Ship ship, int x, int y)
    {
        int startY = y == 0 ? 0 : y - 1;
        int startX = x == 0 ? 0 : y - 1;
        int endX;
        int endY;
        int shipSize = ship.getSize();

        if (ship.isVertical()) {
            endX = x == size - 1 ? x : x + 1;
            endY = y + shipSize;
            endY = endY == size - 1 ? endY : endY + 1;
        } else {
            endY = y == size - 1 ? y : y + 1;
            endX = x + shipSize;
            endX = endX == size - 1 ? endX : endX + 1;
        }

        for (int i = startX; i < endX; i++) {
            for (int j = startY; j < endY; j++) {
                availableFields[i][j] = false;
            }
        }
    }

    private boolean requestedFieldsOutOfBoard(Ship ship, int x, int y)
    {
        return x >= size
                || y >= size
                || (ship.isVertical() && y + ship.getSize() >= size)
                || (!ship.isVertical() && x + ship.getSize() >= size);
    }

    private boolean requestedFieldsFree(Ship ship, int x, int y)
    {
        if (ship.isVertical()) {
            int shipEndY = y + ship.getSize();

            for (int i = y; i < shipEndY; i++) {
                if (!availableFields[x][i]) {
                    return false;
                }
            }
        } else {
            int shipEndX = x + ship.getSize();

            for (int i = x; i < shipEndX; i++) {
                if (!availableFields[i][y]) {
                    return false;
                }
            }
        }

        return true;
    }

    private void placeShip(Ship ship, int x, int y)
    {
        ship.setStartPositionX(x);
        ship.setStartPositionY(y);

        if (ship.isVertical()) {
            int shipEndY = y + ship.getSize();

            for (int i = y; i < shipEndY; i++) {
                ships[x][i] = ship;
            }
        } else {
            int shipEndX = x + ship.getSize();

            for (int i = x; i < shipEndX; i++) {
                ships[x][i] = ship;
            }
        }
    }
}
