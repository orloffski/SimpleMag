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
import modes.TabMode;
import utils.NodeGeneratorUtils;
import view.settings.CounterpartiesDirectoryViewController;

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
        try {
            NodeGeneratorUtils.openNewTab(rootTab, TabMode.UNITS_LIST, main);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openCounterpartiesSettings(){
        try {
            NodeGeneratorUtils.openNewTab(rootTab, TabMode.COUNTERPARTIES_LIST, main);
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
