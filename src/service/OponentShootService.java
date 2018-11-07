package service;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import model.board.Board;
import model.ship.Ship;
import utils.ButtonStyleInterface;

import java.util.Random;

public class OponentShootService extends ShootService {
    private Random random = new Random();

    public OponentShootService(GridPane gridPane, Board board) {
        super(gridPane, board);
    }

    public void shoot()
    {
        int row;
        int column;

        do {
            row = random.nextInt(board.getSize());
            column = random.nextInt(board.getSize());
        } while (board.isFieldShooted(row, column));

        Button button = (Button) GridPaneNodeFinder.getNodeByRowColumnIndex(row, column, gridPane);

        Ship ship = board.shoot(row, column);

        if (ship == null) {
            button.setStyle(ButtonStyleInterface.STYLE_MISSED);
        } else if (ship.isSunk()) {
            markShipAsSunk(ship);

            if (shipSunkEventListener != null) shipSunkEventListener.onShipSunk(ship, gridPane, board);
        } else {
            button.setStyle(ButtonStyleInterface.STYLE_DAMAGED);
        }
    }
}
