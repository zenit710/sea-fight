package sample;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import model.Ship;
import model.ShipPlacer;
import model.ShipListFactory;
import model.board.Board;

import java.util.ArrayList;

public class Controller {
    private final int boardSize = 10;

    @FXML
    private GridPane root;

    @FXML
    private GridPane oponentPane;

    @FXML
    private GridPane playerPane;

    private ShipListFactory shipListFactory;
    private Board oponentsBoard;
    private Board playersBoard;

    @FXML
    public void initialize() {
        oponentsBoard = new Board(boardSize);
        playersBoard = new Board(boardSize);

        shipListFactory = new ShipListFactory(4, 3, 2, 1);

        ShipPlacer shipPlacer = new ShipPlacer(oponentsBoard);
        shipPlacer.placeShips(shipListFactory.create());

        letPlayerChooseShipPositions();
        initShotButtons();
    }

    private void letPlayerChooseShipPositions()
    {
        ArrayList<Ship> shipList = shipListFactory.create();

        initShipPlaceButtons(shipList, 0);
    }

    private void initShipPlaceButtons(ArrayList<Ship> shipList, int shipNumber)
    {
        Ship currentShip = shipList.get(shipNumber);
        boolean[][] availableFields = playersBoard.getAvailableFields();
        Ship[][] ships = playersBoard.getShips();

        playerPane.getChildren().clear();

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                Button btn = new Button();

                if (ships[i][j] != null) {
                    btn.setStyle("-fx-background-color: #0000ff;");
                }

                if (!availableFields[i][j]) {
                    btn.setDisable(true);
                }

                btn.setOnMouseEntered(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        int row = GridPane.getRowIndex(btn);
                        int column = GridPane.getColumnIndex(btn);

                        focusShipLocation(currentShip, row, column);
                    }
                });

                btn.setOnMouseExited(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        int row = GridPane.getRowIndex(btn);
                        int column = GridPane.getColumnIndex(btn);

                        focusOffShipLocation(currentShip, row, column);
                    }
                });

                btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        int row = GridPane.getRowIndex(btn);
                        int column = GridPane.getColumnIndex(btn);

                        if (event.getButton() == MouseButton.SECONDARY) {
                            focusOffShipLocation(currentShip, row, column);

                            currentShip.changeOrientation();

                            focusShipLocation(currentShip, row, column);
                        } else {
                            try {
                                playersBoard.addShip(currentShip, row, column);

                                if (shipNumber < shipList.size() - 1) {
                                    initShipPlaceButtons(shipList, shipNumber + 1);
                                } else {
                                    displayPlayerShips();
                                }
                            } catch(Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

                playerPane.add(btn, j, i);
            }
        }
    }

    private void focusShipLocation(Ship ship, int row, int column)
    {
        if (ship.isVertical()) {
            int endRow = row + ship.getSize();

            if (endRow <= boardSize) {
                for (int i = row; i < endRow; i++) {
                    Button button = (Button) getNodeByRowColumnIndex(i, column, playerPane);
                    button.setStyle("-fx-background-color: #0000ff;");
                }
            }
        } else {
            int endColumn = column + ship.getSize();

            if (endColumn <= boardSize) {
                for (int i = column; i < endColumn; i++) {
                    Button button = (Button) getNodeByRowColumnIndex(row, i, playerPane);
                    button.setStyle("-fx-background-color: #0000ff;");
                }
            }
        }
    }

    private void focusOffShipLocation(Ship ship, int row, int column)
    {
        if (ship.isVertical()) {
            int endRow = row + ship.getSize();

            if (endRow <= boardSize) {
                for (int i = row; i < endRow; i++) {
                    Button button = (Button) getNodeByRowColumnIndex(i, column, playerPane);
                    button.setStyle(null);
                }
            }
        } else {
            int endColumn = column + ship.getSize();

            if (endColumn <= boardSize) {
                for (int i = column; i < endColumn; i++) {
                    Button button = (Button) getNodeByRowColumnIndex(row, i, playerPane);
                    button.setStyle(null);
                }
            }
        }
    }

    private Node getNodeByRowColumnIndex (final int row, final int column, GridPane gridPane) {
        ObservableList<Node> childrens = gridPane.getChildren();

        for (Node node : childrens) {
            if (GridPane.getRowIndex(node) != null
                    && GridPane.getColumnIndex(node) != null
                    && GridPane.getRowIndex(node) == row
                    && GridPane.getColumnIndex(node) == column
            ) {
                return node;
            }
        }

        return null;
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
                            btn.setStyle("-fx-background-color: #ff0000; ");

                            if (ship.isSunk()) {
                                System.out.println("trafiony - zatopiony");
                            }
                        }

                        btn.setDisable(true);
                    }
                });

                oponentPane.add(btn, j, i);
            }
        }
    }
}
