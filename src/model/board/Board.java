package model.board;

import model.Coord;
import model.Range;
import model.ship.Ship;

public class Board {
    private int size;
    private String name;
    private Ship[][] ships;
    private boolean[][] availableFields;
    private boolean[][] shots;
    private boolean[][] sensibleShots;

    public Board(int size, String name)
    {
        this.size = size;
        this.name = name;
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
        permitFields(ship);
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

    public boolean[][] getAvailableFields() {
        return availableFields;
    }

    public String getName() {
        return name;
    }

    public Ship getShipAt(int row, int column) {
        return ships[row][column];
    }

    public Ship[][] getShips() {
        return ships;
    }

    public int getSize() {
        return size;
    }

    public boolean isFieldShooted(int row, int column)
    {
        return shots[row][column];
    }

    public boolean isShootSensible(int row, int column) {
        return sensibleShots[row][column];
    }

    public Ship shoot(int row, int column)
    {
        shots[row][column] = true;

        if (ships[row][column] != null) {
            Ship ship = ships[row][column];
            ship.hit();

            if (ship.isSunk()) markSenselessFields(ship);

            return ship;
        }

        return null;
    }

    private void clearBoard()
    {
        ships = new Ship[size][size];
        availableFields = new boolean[size][size];
        shots = new boolean[size][size];
        sensibleShots = new boolean[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                ships[i][j] = null;
                availableFields[i][j] = true;
                shots[i][j] = false;
                sensibleShots[i][j] = true;
            }
        }
    }

    private void permitFields(Ship ship) {
        Range range = getRange(ship);
        Coord from = range.getFrom();
        Coord to = range.getTo();

        for (int i = from.getRow(); i <= to.getRow(); i++) {
            for (int j = from.getColumn(); j <= to.getColumn(); j++) {
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

    private void markSenselessFields(Ship ship) {
        Range range = getRange(ship);
        Coord from = range.getFrom();
        Coord to = range.getTo();

        for (int i = from.getRow(); i <= to.getRow(); i++) {
            for (int j = from.getColumn(); j <= to.getColumn(); j++) {
                sensibleShots[i][j] = false;
            }
        }

        System.out.println("");
        for (int i = 0; i < size; i++) {
            System.out.println("");
            for (int j = 0; j < size; j++) {
                System.out.print(" " + (sensibleShots[i][j] ? "O" : "X"));
            }
        }
    }

    private Range getRange(Ship ship) {
        int column = ship.getStartColumn();
        int row = ship.getStartRow();
        int startColumn = column == 0 ? 0 : column - 1;
        int startRow = row == 0 ? 0 : row - 1;
        int endRow;
        int endColumn;
        int shipSize = ship.getSize();

        if (ship.isVertical()) {
            endColumn = column == size - 1 ? column : column + 1;
            endRow = row + shipSize;
            endRow = endRow == size ? endRow - 1 : endRow;
        } else {
            endRow = row == size - 1 ? row : row + 1;
            endColumn = column + shipSize;
            endColumn = endColumn == size ? endColumn - 1 : endColumn;
        }

        return new Range(
            new Coord(startRow, startColumn),
            new Coord(endRow, endColumn)
        );
    }
}
