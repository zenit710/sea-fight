package gameplay.event;

import javafx.scene.layout.GridPane;
import model.board.Board;
import model.ship.Ship;

public interface ShipSunkEventListener {
    void onShipSunk(Ship ship, GridPane gridPane, Board board);
}
