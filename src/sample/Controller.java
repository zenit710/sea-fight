package sample;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
                    btn.setStyle("-fx-background-color: #ff0000;");
                }

                if (!availableFields[i][j]) {
                    btn.setDisable(true);
                }

                btn.setOnMouseEntered(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        int row = GridPane.getRowIndex(btn);
                        int column = GridPane.getColumnIndex(btn);

                        if (currentShip.isVertical()) {
                            int endRow = row + currentShip.getSize();

                            if (endRow <= boardSize) {
                                for (int i = row; i < endRow; i++) {
                                    Button button = (Button) getNodeByRowColumnIndex(i, column, playerPane);
                                    button.setStyle("-fx-background-color: #ff0000;");
                                }
                            }
                        } else {
                            int endColumn = column + currentShip.getSize();

                            if (endColumn <= boardSize) {
                                for (int i = column; i < endColumn; i++) {
                                    Button button = (Button) getNodeByRowColumnIndex(row, i, playerPane);
                                    button.setStyle("-fx-background-color: #ff0000;");
                                }
                            }
                        }
                    }
                });

                btn.setOnMouseExited(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        int row = GridPane.getRowIndex(btn);
                        int column = GridPane.getColumnIndex(btn);

                        if (currentShip.isVertical()) {
                            int endRow = row + currentShip.getSize();

                            if (endRow <= boardSize) {
                                for (int i = row; i < endRow; i++) {
                                    Button button = (Button) getNodeByRowColumnIndex(i, column, playerPane);
                                    button.setStyle(null);
                                }
                            }
                        } else {
                            int endColumn = column + currentShip.getSize();

                            if (endColumn <= boardSize) {
                                for (int i = column; i < endColumn; i++) {
                                    Button button = (Button) getNodeByRowColumnIndex(row, i, playerPane);
                                    button.setStyle(null);
                                }
                            }
                        }
                    }
                });

                btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        int row = GridPane.getRowIndex(btn);
                        int column = GridPane.getColumnIndex(btn);

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
                });

                playerPane.add(btn, j, i);
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
                Label label = new Label();

                if (ships[i][j] != null) {
                    label.setText("O");
                }

                playerPane.add(label, j, i);
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
