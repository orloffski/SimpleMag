package view;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import view.stockviews.settings.CounterpartiesDirectoryViewController;
import view.stockviews.settings.UnitsDirectoryViewController;

import java.io.IOException;

public class SettingsViewController extends AbstractRootController{

    @FXML
    private Text unitsSettings;

    @FXML
    private Text counterpartiesSettings;

    public SettingsViewController() {
    }

    @FXML
    private void initialize() {
    }

    @FXML
    private void openUnitsSettings(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/view/stockviews/settings/UnitsDirectoryView.fxml"));
        try {
            BorderPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Единицы измерения");
            dialogStage.getIcons().add(new Image("file:resources/images/goods.png"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(main.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            UnitsDirectoryViewController unitController = loader.getController();
            unitController.setDialogStage(dialogStage);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openCounterpartiesSettings(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/view/stockviews/settings/CounterpartiesDirectoryView.fxml"));
        try {
            BorderPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Контрагенты");
            dialogStage.getIcons().add(new Image("file:resources/images/counterparties.png"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(main.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            CounterpartiesDirectoryViewController counterpartiesController = loader.getController();
            counterpartiesController.setDialogStage(dialogStage);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void setUnitsTextUnderline(){
        unitsSettings.setUnderline(true);
    }

    @FXML
    private void removeUnitsTextUnderline(){
        unitsSettings.setUnderline(false);
    }

    @FXML
    private void setCounterpartiesTextUnderline(){
        counterpartiesSettings.setUnderline(true);
    }

    @FXML
    private void removeCounterpartiesTextUnderline(){
        counterpartiesSettings.setUnderline(false);
    }
}
