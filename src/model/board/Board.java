package model.board;

import model.Ship;

public class Board {
    private int size;
    private Ship[][] ships;
    private boolean[][] availableFields;
    private boolean[][] shots;

    public Board(int size)
    {
        this.size = size;
        clearBoard();
    }

    public void addShip(Ship ship, int row, int column) throws OutOfBoardException, BoardFieldsPermittedException
    {
        if (requestedFieldsOutOfBoard(ship, row, column)) {
            throw new OutOfBoardException("Ship out of board");
        }

        if (!requestedFieldsFree(ship, row, column)) {
            throw new BoardFieldsPermittedException("Fields are not available");
        }

        ship.setStartColumn(column);
        ship.setStartRow(row);

        placeShip(ship, row, column);
        permitFields(ship, row, column);
    }

    public boolean allShipsSunk() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Ship ship = ships[i][j];

                if (ship != null && !ship.isSunk()) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean isFieldShooted(int row, int column)
    {
        return shots[row][column];
    }

    public boolean[][] getAvailableFields() {
        return availableFields;
    }

    public Ship[][] getShips() {
        return ships;
    }

    public int getSize() {
        return size;
    }

    public Ship shoot(int row, int column)
    {
        shots[row][column] = true;

        if (ships[row][column] != null) {
            Ship ship = ships[row][column];
            ship.hit();

            return ship;
        }

        return null;
    }

    private void clearBoard()
    {
        ships = new Ship[size][size];
        availableFields = new boolean[size][size];
        shots = new boolean[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                ships[i][j] = null;
                availableFields[i][j] = true;
                shots[i][j] = false;
            }
        }
    }

    private void permitFields(Ship ship, int row, int column)
    {
        int startColumn = column == 0 ? 0 : column - 1;
        int startRow = row == 0 ? 0 : row - 1;
        int endRow;
        int endColumn;
        int shipSize = ship.getSize();

        if (ship.isVertical()) {
            endColumn = column == size - 1 ? column : column + 1;
            endRow = startRow + shipSize;
            endRow = endRow == size - 1 ? endRow : endRow + 1;
        } else {
            endRow = row == size - 1 ? row : row + 1;
            endColumn = startColumn + shipSize;
            endColumn = endColumn == size - 1 ? endColumn : endColumn + 1;
        }

        for (int i = startRow; i <= endRow; i++) {
            for (int j = startColumn; j <= endColumn; j++) {
                availableFields[i][j] = false;
            }
        }
    }

    private boolean requestedFieldsOutOfBoard(Ship ship, int row, int column)
    {
        return row >= size
                || column >= size
                || (ship.isVertical() && row + ship.getSize() > size)
                || (!ship.isVertical() && column + ship.getSize() > size);
    }

    private boolean requestedFieldsFree(Ship ship, int row, int column)
    {
        if (ship.isVertical()) {
            int shipEndRow = row + ship.getSize();

            for (int i = row; i < shipEndRow; i++) {
                if (!availableFields[i][column]) {
                    return false;
                }
            }
        } else {
            int shipEndColumn = column + ship.getSize();

            for (int i = column; i < shipEndColumn; i++) {
                if (!availableFields[row][i]) {
                    return false;
                }
            }
        }

        return true;
    }

    private void placeShip(Ship ship, int row, int column)
    {
        if (ship.isVertical()) {
            int shipEndRow = row + ship.getSize();

            for (int i = row; i < shipEndRow; i++) {
                ships[i][column] = ship;
            }
        } else {
            int shipEndColumn = column + ship.getSize();

            for (int i = column; i < shipEndColumn; i++) {
                ships[row][i] = ship;
            }
        }
    }
}
