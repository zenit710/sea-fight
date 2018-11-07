package service;

import javafx.scene.control.Label;

public class MessageService {
    private Label messageLabel;

    public MessageService(Label messageLabel) {
        this.messageLabel = messageLabel;
    }

    public void showMessage(String message) {
        messageLabel.setText(message);
    }

    public void clear() {
        messageLabel.setText("");
    }
}
