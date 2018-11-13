package service;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MessageService {
    private VBox messageContainer;

    public MessageService(VBox messageContainer) {
        this.messageContainer = messageContainer;
    }

    public void addMessage(String message) {
        Label label = new Label(message);

        messageContainer.getChildren().add(label);
    }

    public void clear() {
        messageContainer.getChildren().clear();
    }
}
