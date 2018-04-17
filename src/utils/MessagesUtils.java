package utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Optional;

public class MessagesUtils {

    public static final void showAlert(String title, String messageText){
        Alert alert = new Alert(Alert.AlertType.WARNING);

        alert.setTitle(title);
        alert.setContentText(messageText);

        Stage stage = (Stage)alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("file:resources/images/alert.png"));

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

    public static final ButtonType showConfirmAlert(String title, String messageText, String contextText){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle(title);
        alert.setHeaderText(messageText);
        alert.setContentText(contextText);

        Optional<ButtonType> result = alert.showAndWait();

        return result.get();
    }
}
