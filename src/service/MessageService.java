package service;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class MessageService {
    private ScrollPane scrollPane;
    private VBox messageContainer;

    public MessageService(ScrollPane scrollPane, VBox messageContainer) {
        this.scrollPane = scrollPane;
        this.messageContainer = messageContainer;
    }

    public void addMessage(String message) {
        Label label = new Label(message);

        messageContainer.getChildren().add(label);
        scrollPane.setVvalue(1.0); // scroll to bottom
    }

    public void addMessage(String message, String color) {
        Label label = new Label(message);
        label.setTextFill(Color.web(color));

        messageContainer.getChildren().add(label);
        scrollPane.setVvalue(1.0); // scroll to bottom
    }

    public void clear() {
        messageContainer.getChildren().clear();
    }
}
