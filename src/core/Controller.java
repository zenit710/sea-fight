package core;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import model.board.Board;
import model.ship.Ship;
import model.ship.ShipListFactory;
import service.*;
import utils.ButtonStyleInterface;

import java.util.ArrayList;

public class Controller implements ShipsPlacedEventListener, ShipSunkEventListener, ShootEventListener {
    private final int boardSize = 10;

    @FXML
    private GridPane oponentPane;

    @FXML
    private GridPane playerPane;

    @FXML
    private Label statusLabel;

    private ShipListFactory shipListFactory;
    private Board oponentsBoard;
    private Board playersBoard;
    private MessageService messageService;
    private OponentShootService oponentShootService;

    @FXML
    public void initialize() {
        messageService = new MessageService(statusLabel);

        oponentsBoard = new Board(boardSize, "oponentsBoard");
        playersBoard = new Board(boardSize, "playersBoard");

        shipListFactory = new ShipListFactory(4, 3, 2, 1);

        ShipPlacer shipPlacer = new ShipPlacer(oponentsBoard);
        shipPlacer.placeShips(shipListFactory.create());

        letPlayerChooseShipPositions();
    }

    private void letPlayerChooseShipPositions()
    {
        ArrayList<Ship> shipList = shipListFactory.create();

        ShipPlaceService shipPlaceService = new ShipPlaceService(playerPane, playersBoard, shipList, messageService);
        shipPlaceService.setShipPlacedEventListener(this);
        shipPlaceService.askUserToPlaceShips();
    }

    private void play()
    {
        messageService.clear();
        displayPlayerShips();

        PlayerShootService playerShootService = new PlayerShootService(oponentPane, oponentsBoard);
        playerShootService.setShipSunkEventListener(this);
        playerShootService.setShootEventListener(this);
        playerShootService.initShotButtons();

        oponentShootService = new OponentShootService(playerPane, playersBoard);
        oponentShootService.setShipSunkEventListener(this);
    }

    @Override
    public void onShipPlaced() {
        play();
    }

    @Override
    public void onShipSunk(Ship ship, GridPane gridPane, Board board) {
        if (board.allShipsSunk()) {
            if (board.getName().equals(oponentsBoard.getName())) {
                messageService.showMessage("WINNER!");
            } else {
                messageService.showMessage("You lost...");
            }
        }
    }

    @Override
    public void onShoot() {
        oponentShootService.shoot();
    }

    private void displayPlayerShips()
    {
        Ship[][] ships = playersBoard.getShips();

        playerPane.getChildren().clear();

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                Button btn = new Button();
                btn.setDisable(true);

                if (ships[i][j] != null) {
                    btn.setStyle(ButtonStyleInterface.STYLE_OK);
                }

                playerPane.add(btn, j, i);
            }
        }
    }
}
