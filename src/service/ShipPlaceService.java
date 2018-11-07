package service;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import model.ship.Ship;
import model.board.Board;

import java.util.ArrayList;

public class ShipPlaceService {
    private final String STYLE_FOCUSED = "-fx-background-color: #0000ff;";

    private ShipsPlacedEventListener listener;
    private GridPane gridPane;
    private Board board;
    private ArrayList<Ship> shipList;
    private MessageService messageService;

    public ShipPlaceService(GridPane gridPane, Board board, ArrayList<Ship> shipList, MessageService messageService) {
        this.gridPane = gridPane;
        this.board = board;
        this.shipList = shipList;
        this.messageService = messageService;
    }

    public void askUserToPlaceShips() {
        initShipPlaceButtons(0);
    }

    public void setShipPlacedEventListener(ShipsPlacedEventListener listener) {
        this.listener = listener;
    }

    private void initShipPlaceButtons(int shipNumber)
    {
        Ship currentShip = shipList.get(shipNumber);
        boolean[][] availableFields = board.getAvailableFields();
        Ship[][] ships = board.getShips();

        showStatusMessage(currentShip, shipNumber + 1);

        gridPane.getChildren().clear();

        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                Button btn = new Button();

                if (ships[i][j] != null) {
                    btn.setStyle(STYLE_FOCUSED);
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
                            showStatusMessage(currentShip, shipNumber + 1);

                            focusShipLocation(currentShip, row, column);
                        } else {
                            try {
                                board.addShip(currentShip, row, column);

                                if (shipNumber < shipList.size() - 1) {
                                    initShipPlaceButtons(shipNumber + 1);
                                } else {
                                    if (listener != null) listener.onShipPlaced();
                                }
                            } catch(Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

                gridPane.add(btn, j, i);
            }
        }
    }

    private void focusShipLocation(Ship ship, int row, int column)
    {
        if (ship.isVertical()) {
            int endRow = row + ship.getSize();

            if (endRow <= board.getSize()) {
                for (int i = row; i < endRow; i++) {
                    Button button = (Button) GridPaneNodeFinder.getNodeByRowColumnIndex(i, column, gridPane);
                    button.setStyle(STYLE_FOCUSED);
                }
            }
        } else {
            int endColumn = column + ship.getSize();

            if (endColumn <= board.getSize()) {
                for (int i = column; i < endColumn; i++) {
                    Button button = (Button) GridPaneNodeFinder.getNodeByRowColumnIndex(row, i, gridPane);
                    button.setStyle(STYLE_FOCUSED);
                }
            }
        }
    }

    private void focusOffShipLocation(Ship ship, int row, int column)
    {
        if (ship.isVertical()) {
            int endRow = row + ship.getSize();

            if (endRow <= board.getSize()) {
                for (int i = row; i < endRow; i++) {
                    Button button = (Button) GridPaneNodeFinder.getNodeByRowColumnIndex(i, column, gridPane);
                    button.setStyle(null);
                }
            }
        } else {
            int endColumn = column + ship.getSize();

            if (endColumn <= board.getSize()) {
                for (int i = column; i < endColumn; i++) {
                    Button button = (Button) GridPaneNodeFinder.getNodeByRowColumnIndex(row, i, gridPane);
                    button.setStyle(null);
                }
            }
        }
    }

    private void showStatusMessage(Ship ship, int shipNumber) {
        String statusMessage = "Place next ship (" + shipNumber + "):\n";
        statusMessage += "Size: " + ship.getSize() + "\n";
        statusMessage += "Orientation: ";
        statusMessage += ship.isVertical() ? "Vertical" : "Horizontal";

        messageService.showMessage(statusMessage);
    }
}
