package service;

import javafx.scene.layout.GridPane;
import model.ship.Ship;

public interface ShipSunkEventListener {
    void onShipSunk(Ship ship, GridPane gridPane);
}
