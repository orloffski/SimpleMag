package utils;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MessagesUtils {

    public static final void showAlert(String title, String messageText){
        Alert alert = new Alert(Alert.AlertType.WARNING);

        alert.setTitle(title);
        alert.setContentText(messageText);

        alert.showAndWait();
    }

    public static final void showShortInfo(String title, String messageText){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(messageText);

        Stage stage = (Stage)alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("file:resources/images/success.png"));

        alert.showAndWait();
    }
}
