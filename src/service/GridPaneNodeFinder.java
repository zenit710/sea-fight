package service;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class GridPaneNodeFinder {
    public static Node getNodeByRowColumnIndex (final int row, final int column, GridPane gridPane) {
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
}
