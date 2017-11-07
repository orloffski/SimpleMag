package view;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import modes.TabMode;
import utils.NodeGeneratorUtils;

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
