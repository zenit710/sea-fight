package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import model.Ship;
import model.ShipPlacer;
import model.board.Board;

public class Controller {
    private final int boardSize = 10;

    @FXML
    private GridPane oponentPane;

    @FXML
    private GridPane playerPane;

    Board oponentsBoard;
    Board playersBoard;

    @FXML
    public void initialize() {
        oponentsBoard = new Board(boardSize);

        ShipPlacer shipPlacer = new ShipPlacer(oponentsBoard, 4, 3, 2, 1);
        shipPlacer.placeShips();

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
