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
    private CheckBox invoiceFromStock;

    @FXML
    private CheckBox sellsFromStock;

    @FXML
    private CheckBox autoVat;

    @FXML
    private CheckBox autoExtraPrice;

    @FXML
    public void initialize(){
        loadSettings(SettingsEngine.getInstance().getSettings());
        openSettings(productsInStock.isSelected());
    }

    @FXML
    private void setProductsInStock(){
        SettingsEngine.getInstance().getSettings().productsInStockEnabled = productsInStock.isSelected();

        openSettings(productsInStock.isSelected());
    }

    @FXML
    private void setInvoicesFromStock(){
        SettingsEngine.getInstance().getSettings().invoicesFromStock = invoiceFromStock.isSelected();
    }

    @FXML
    private void setSellsFromStock(){
        SettingsEngine.getInstance().getSettings().sellsFromStock = sellsFromStock.isSelected();
    }

    @FXML
    private void setAutoVat(){
        SettingsEngine.getInstance().getSettings().autoVat = autoVat.isSelected();
    }

    @FXML
    private void setAutoExtraPrice(){
        SettingsEngine.getInstance().getSettings().autoExtraPrice = autoExtraPrice.isSelected();
    }

    @Override
    protected void clearForm() {

    }

    private void loadSettings(SettingsModel settings){
        productsInStock.setSelected(settings.productsInStockEnabled);
        invoiceFromStock.setSelected(settings.invoicesFromStock);
        sellsFromStock.setSelected(settings.sellsFromStock);
        autoVat.setSelected(settings.autoVat);
        autoExtraPrice.setSelected(settings.autoExtraPrice);
    }

    private void openSettings(boolean isProductsInStockEnabled){
        if(isProductsInStockEnabled){
            invoiceFromStock.setDisable(false);
            sellsFromStock.setDisable(false);
        }else{
            invoiceFromStock.setDisable(true);
            sellsFromStock.setDisable(true);
        }
    }
}
