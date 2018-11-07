package service;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import model.board.Board;
import model.ship.Ship;
import utils.ButtonStyleInterface;

public class PlayerShootService {
    private ShipSunkEventListener shipSunkEventListener;
    private ShootEventListener shootEventListener;
    private GridPane gridPane;
    private Board board;

    public PlayerShootService(GridPane gridPane, Board board) {
        this.gridPane = gridPane;
        this.board = board;
    }

    public void initShotButtons()
    {
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                Button btn = new Button();

                btn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Ship ship = board.shoot(GridPane.getRowIndex(btn), GridPane.getColumnIndex(btn));

                        if (ship != null) {
                            btn.setStyle(ButtonStyleInterface.STYLE_DAMAGED);

                            if (ship.isSunk()) {
                                if (shipSunkEventListener != null) shipSunkEventListener.onShipSunk(ship, gridPane);
                            }
                        } else {
                            btn.setStyle(ButtonStyleInterface.STYLE_MISSED);
                        }

                        btn.setDisable(true);

                        if (shootEventListener != null) shootEventListener.onShoot();
                    }
                });

                gridPane.add(btn, j, i);
            }
        }
    }

    public void setShipSunkEventListener(ShipSunkEventListener listener) {
        this.shipSunkEventListener = listener;
    }

    public void setShootEventListener(ShootEventListener shootEventListener) {
        this.shootEventListener = shootEventListener;
    }
}