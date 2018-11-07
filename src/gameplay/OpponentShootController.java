package gameplay;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import model.Coord;
import model.board.Board;
import model.ship.Ship;
import service.GridPaneNodeFinder;
import utils.ButtonStyleInterface;

import java.util.Random;

public class OpponentShootController extends ShootController {
    private Random random = new Random();
    private Coord lastShipCoord;

    public OpponentShootController(GridPane gridPane, Board board) {
        super(gridPane, board);
    }

    public void shoot()
    {
        Coord coord = getShootCoord();

        Button button = (Button) GridPaneNodeFinder.getNodeByRowColumnIndex(coord.getRow(), coord.getColumn(), gridPane);

        Ship ship = board.shoot(coord.getRow(), coord.getColumn());

        if (ship == null) {
            button.setStyle(ButtonStyleInterface.STYLE_MISSED);
        } else if (ship.isSunk()) {
            lastShipCoord = null;
            markShipAsSunk(ship);

            if (shipSunkEventListener != null) shipSunkEventListener.onShipSunk(ship, gridPane, board);
        } else {
            button.setStyle(ButtonStyleInterface.STYLE_DAMAGED);

            if (lastShipCoord == null) lastShipCoord = coord;
        }
    }

    private Coord getShootCoord() {
        Coord coord = null;

        if (lastShipCoord != null) {
            coord = tryUp();

            if (coord == null) {
                coord = tryDown();
            }

            if (coord == null) {
                coord = tryLeft();
            }

            if (coord == null) {
                coord = tryRight();
            }
        }

        if (coord == null) {
            coord = tryRandom();
        }

        return coord;
    }

    private Coord tryDown() {
        int row = lastShipCoord.getRow() + 1;
        int column = lastShipCoord.getColumn();

        while (row < board.getSize()) {
            if (!board.isShootSensible(row, column)) {
                return null;
            } else if (board.isFieldShooted(row, column)) {
                Ship ship = board.getShipAt(row, column);

                if (ship == null) {
                    return null;
                }
            } else {
                return new Coord(row, column);
            }

            row++;
        }

        return null;
    }

    private Coord tryLeft() {
        int row = lastShipCoord.getRow();
        int column = lastShipCoord.getColumn() - 1;

        while (column >= 0) {
            if (!board.isShootSensible(row, column)) {
                return null;
            } else if (board.isFieldShooted(row, column)) {
                Ship ship = board.getShipAt(row, column);

                if (ship == null) {
                    return null;
                }
            } else {
                return new Coord(row, column);
            }

            column--;
        }

        return null;
    }

    private Coord tryRandom() {
        int row;
        int column;

        do {
            row = random.nextInt(board.getSize());
            column = random.nextInt(board.getSize());
        } while (!board.isShootSensible(row, column) || board.isFieldShooted(row, column));

        return new Coord(row, column);
    }

    private Coord tryRight() {
        int row = lastShipCoord.getRow();
        int column = lastShipCoord.getColumn() + 1;

        while (column < board.getSize()) {
            if (!board.isShootSensible(row, column)) {
                return null;
            } else if (board.isFieldShooted(row, column)) {
                Ship ship = board.getShipAt(row, column);

                if (ship == null) {
                    return null;
                }
            } else {
                return new Coord(row, column);
            }

            column++;
        }

        return null;
    }

    private Coord tryUp() {
        int row = lastShipCoord.getRow() - 1;
        int column = lastShipCoord.getColumn();

        while (row >= 0) {
            if (!board.isShootSensible(row, column)) {
                return null;
            } else if (board.isFieldShooted(row, column)) {
                Ship ship = board.getShipAt(row, column);

                if (ship == null) {
                    return null;
                }
            } else {
                return new Coord(row, column);
            }

            row--;
        }

        return null;
    }
}
