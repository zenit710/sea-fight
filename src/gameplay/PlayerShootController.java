package gameplay;

import gameplay.event.ShootEventListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import model.board.Board;
import model.ship.Ship;
import service.MessageService;
import utils.ButtonStyleInterface;

public class PlayerShootController extends ShootController {
    private ShootEventListener shootEventListener;

    public PlayerShootController(GridPane gridPane, Board board, MessageService messageService) {
        super(gridPane, board, messageService);
    }

    public void initShotButtons()
    {
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                Button btn = new Button();

                btn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        int row = GridPane.getRowIndex(btn);
                        int column = GridPane.getColumnIndex(btn);

                        Ship ship = board.shoot(row, column);
                        String shootMessage = "Player shooted at: " + column + "x" + row;

                        if (ship != null) {
                            btn.setStyle(ButtonStyleInterface.STYLE_DAMAGED);
                            shootMessage += " - hit";

                            if (ship.isSunk()) {
                                markShipAsSunk(ship);
                                shootMessage += " - sunk";

                                if (shipSunkEventListener != null) shipSunkEventListener.onShipSunk(ship, gridPane, board);
                            }
                        } else {
                            btn.setStyle(ButtonStyleInterface.STYLE_MISSED);
                        }

                        btn.setDisable(true);
                        messageService.addMessage(shootMessage);

                        if (shootEventListener != null) shootEventListener.onShoot();
                    }
                });

                gridPane.add(btn, j, i);
            }
        }
    }

    public void setShootEventListener(ShootEventListener shootEventListener) {
        this.shootEventListener = shootEventListener;
    }
}
