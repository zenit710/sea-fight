package gameplay;

import gameplay.event.ShipSunkEventListener;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import model.board.Board;
import model.ship.Ship;
import service.GridPaneNodeFinder;
import service.MessageService;
import utils.ButtonStyleInterface;

public abstract class ShootController {
    protected ShipSunkEventListener shipSunkEventListener;
    protected GridPane gridPane;
    protected MessageService messageService;
    protected Board board;

    public ShootController(GridPane gridPane, Board board, MessageService messageService) {
        this.gridPane = gridPane;
        this.board = board;
        this.messageService = messageService;
    }

    protected void markShipAsSunk(Ship ship)
    {
        if (ship.isVertical()) {
            int endRow = ship.getStartRow() + ship.getSize();

            for (int i = ship.getStartRow(); i < endRow; i++) {
                Button button = (Button) GridPaneNodeFinder.getNodeByRowColumnIndex(i, ship.getStartColumn(), gridPane);
                button.setStyle(ButtonStyleInterface.STYLE_DESTROYED);
            }
        } else {
            int endColumn = ship.getStartColumn() + ship.getSize();

            for (int i = ship.getStartColumn(); i < endColumn; i++) {
                Button button = (Button) GridPaneNodeFinder.getNodeByRowColumnIndex(ship.getStartRow(), i, gridPane);
                button.setStyle(ButtonStyleInterface.STYLE_DESTROYED);
            }
        }
    }

    public void setShipSunkEventListener(ShipSunkEventListener shipSunkEventListener) {
        this.shipSunkEventListener = shipSunkEventListener;
    }
}
