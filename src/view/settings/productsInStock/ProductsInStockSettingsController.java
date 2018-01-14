package view.settings.productsInStock;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import utils.settingsEngine.SettingsEngine;
import utils.settingsEngine.SettingsModel;
import view.AbstractController;

public class ProductsInStockSettingsController extends AbstractController {

    @FXML
    private CheckBox productsInStock;

    @FXML
    public void initialize(){
        SettingsModel settings = SettingsEngine.getInstance().getSettings();

        productsInStock.setSelected(settings.productsInStockEnabled);
    }

    @FXML
    private void setProductsInStock(){
        SettingsEngine.getInstance().getSettings().productsInStockEnabled = productsInStock.isSelected();
    }

    @Override
    protected void clearForm() {

    }
}
