package utils;

import javafx.scene.control.Alert;

public class MessagesUtils {

    public static final void showAlert(String title, String messageText){
        Alert alert = new Alert(Alert.AlertType.WARNING);

        alert.setTitle(title);
        alert.setContentText(messageText);

        alert.showAndWait();
    }
}
