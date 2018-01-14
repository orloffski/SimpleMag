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

    @FXML
    private Text itemsList;

    @FXML
    private Text productsInStock;

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
    private void openItemsView(){
        try {
            NodeGeneratorUtils.openNewTab(rootTab, TabMode.ITEMS_LIST, main);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openProductsInStockSettings(){
        try {
            NodeGeneratorUtils.openNewTab(rootTab, TabMode.PRODUCTS_IN_STOCK_SETTINGS, main);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void setProductsInStockUnderline(){
        productsInStock.setUnderline(true);
    }

    @FXML
    private void removeProductsInStockUnderline(){
        productsInStock.setUnderline(false);
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

    @FXML
    private void setItemsTextUnderline(){
        itemsList.setUnderline(true);
    }

    @FXML
    private void removeItemsTextUnderline(){
        itemsList.setUnderline(false);
    }
}
