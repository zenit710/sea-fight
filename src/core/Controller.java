package core;

import gameplay.*;
import gameplay.event.ShipSunkEventListener;
import gameplay.event.ShipsPlacedEventListener;
import gameplay.event.ShootEventListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
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
    private ScrollPane messageScrollPane;

    @FXML
    private VBox messageContainer;

    private ShipListFactory shipListFactory;
    private Board oponentsBoard;
    private Board playersBoard;
    private MessageService messageService;
    private OpponentShootController opponentShootController;

    @FXML
    public void initialize() {
        messageScrollPane.setVvalue(1.0);

        messageService = new MessageService(messageScrollPane, messageContainer);
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

        ShipPlaceController shipPlaceController = new ShipPlaceController(playerPane, playersBoard, shipList, messageService);
        shipPlaceController.setShipPlacedEventListener(this);
        shipPlaceController.askUserToPlaceShips();
    }

    private void play()
    {
        messageService.clear();
        displayPlayerShips();

        PlayerShootController playerShootController = new PlayerShootController(oponentPane, oponentsBoard, messageService);
        playerShootController.setShipSunkEventListener(this);
        playerShootController.setShootEventListener(this);
        playerShootController.initShotButtons();

        opponentShootController = new OpponentShootController(playerPane, playersBoard, messageService);
        opponentShootController.setShipSunkEventListener(this);
    }

    @Override
    public void onShipPlaced() {
        play();
    }

    @Override
    public void onShipSunk(Ship ship, GridPane gridPane, Board board) {
        if (board.allShipsSunk()) {
            if (board.getName().equals(oponentsBoard.getName())) {
                messageService.addMessage("YOU WON!", "#00FF00");
            } else {
                messageService.addMessage("You lost...", "#FF0000");
            }

            blockPlayerMoves();
        }
    }

    @Override
    public void onShoot() {
        if (!oponentsBoard.allShipsSunk()) {
            opponentShootController.shoot();
        }
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

    private void blockPlayerMoves() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                ((Button)GridPaneNodeFinder.getNodeByRowColumnIndex(i, j, oponentPane)).setDisable(true);
            }
        }
    }
}
