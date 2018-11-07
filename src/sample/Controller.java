package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import model.board.Board;
import model.ship.Ship;
import model.ship.ShipListFactory;
import service.*;

import java.util.ArrayList;
import java.util.Random;

public class Controller implements ShipsPlacedEventListener {
    private final int boardSize = 10;
    private Random random = new Random();

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

    @FXML
    public void initialize() {
        messageService = new MessageService(statusLabel);

        oponentsBoard = new Board(boardSize);
        playersBoard = new Board(boardSize);

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

    private void displayPlayerShips()
    {
        Ship[][] ships = playersBoard.getShips();

        playerPane.getChildren().clear();

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                Button btn = new Button();
                btn.setDisable(true);

                if (ships[i][j] != null) {
                    btn.setStyle("-fx-background-color: #0000ff;");
                }

                playerPane.add(btn, j, i);
            }
        }
    }

    private void play()
    {
        messageService.clear();
        initShotButtons();
    }

    private void initShotButtons()
    {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                Button btn = new Button();

                btn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Ship ship = oponentsBoard.shoot(GridPane.getRowIndex(btn), GridPane.getColumnIndex(btn));

                        if (ship != null) {
                            btn.setStyle("-fx-background-color: #ffff00; ");

                            if (ship.isSunk()) {
                                markShipAsSunk(ship, oponentPane);
                            }
                        } else {
                            btn.setStyle("-fx-background-color: #000000;");
                        }

                        btn.setDisable(true);

                        if (oponentsBoard.allShipsSunk()) {
                            messageService.showMessage("Wygrałeś!");
                        }

                        oponentShoot();
                    }
                });

                oponentPane.add(btn, j, i);
            }
        }
    }

    private void oponentShoot()
    {
        int row;
        int column;

        do {
            row = random.nextInt(boardSize - 1);
            column = random.nextInt(boardSize - 1);
        } while (playersBoard.isFieldShooted(row, column));

        Button button = (Button) GridPaneNodeFinder.getNodeByRowColumnIndex(row, column, playerPane);

        Ship ship = playersBoard.shoot(row, column);

        if (ship == null) {
            button.setStyle("-fx-background-color: #000000;");
        } else if (ship.isSunk()) {
            markShipAsSunk(ship, playerPane);
        } else {
            button.setStyle("-fx-background-color: #ffff00;");
        }

        if (playersBoard.allShipsSunk()) {
            messageService.showMessage("Przegrałeś!");
        }
    }

    private void markShipAsSunk(Ship ship, GridPane pane)
    {
        if (ship.isVertical()) {
            int endRow = ship.getStartRow() + ship.getSize();

            for (int i = ship.getStartRow(); i < endRow; i++) {
                Button button = (Button) GridPaneNodeFinder.getNodeByRowColumnIndex(i, ship.getStartColumn(), pane);
                button.setStyle("-fx-background-color: #ff0000;");
            }
        } else {
            int endColumn = ship.getStartColumn() + ship.getSize();

            for (int i = ship.getStartColumn(); i < endColumn; i++) {
                Button button = (Button) GridPaneNodeFinder.getNodeByRowColumnIndex(ship.getStartRow(), i, pane);
                button.setStyle("-fx-background-color: #ff0000;");
            }
        }
    }

    @Override
    public void onShipPlaced() {
        displayPlayerShips();
        play();
    }
}
