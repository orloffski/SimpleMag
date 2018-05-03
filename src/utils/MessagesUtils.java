package utils;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.time.LocalDate;
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

    public static final Optional<Pair<LocalDate, LocalDate>> getReportIntervalFromDates(String reportName){
        Dialog<Pair<LocalDate, LocalDate>> dialog = new Dialog<>();
        dialog.setTitle("Выбор дат интервала");
        dialog.setHeaderText("Выберите даты для задания интервала расчета отчета " + reportName);

        ButtonType confirmButton = new ButtonType("Запуск отчета", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        DatePicker dateFrom = new DatePicker();
        dateFrom.setValue(LocalDate.now());
        dateFrom.setShowWeekNumbers(true);

        DatePicker dateTo = new DatePicker();
        dateTo.setValue(LocalDate.now());
        dateTo.setShowWeekNumbers(true);

        grid.add(dateFrom, 0, 0);
        grid.add(dateTo, 1, 0);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButton) {
                return new Pair<>(dateFrom.getValue(), dateTo.getValue());
            }
            return null;
        });

        Optional<Pair<LocalDate, LocalDate>> result = dialog.showAndWait();

        return result;
    }
}
