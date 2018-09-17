package view.settings.productsInStock;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import utils.settingsEngine.SettingsEngine;
import utils.settingsEngine.SettingsModel;
import view.AbstractController;

import java.io.File;

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
    private CheckBox autoBackupEnabled;

    @FXML
    private CheckBox autoBackupOnStart;

    @FXML
    private CheckBox autoBackupOnStop;

    @FXML
    private Button autoBackupPathButton;

    @FXML
    private TextField autoBackupPath;

    @FXML
    public void initialize(){
        loadSettings(SettingsEngine.getInstance().getSettings());
        openSettings(productsInStock.isSelected(), autoBackupEnabled.isSelected());
    }

    @FXML
    private void setProductsInStock(){
        SettingsEngine.getInstance().getSettings().productsInStockEnabled = productsInStock.isSelected();

        openSettings(productsInStock.isSelected(), autoBackupEnabled.isSelected());
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

    @FXML
    private void setAutoBackupEnabled(){
        SettingsEngine.getInstance().getSettings().autoBackupEnabled = autoBackupEnabled.isSelected();

        openSettings(productsInStock.isSelected(), autoBackupEnabled.isSelected());
    }

    @FXML
    private void setAutoBackupOnStart(){
        SettingsEngine.getInstance().getSettings().autoBackupOnStart = autoBackupOnStart.isSelected();
    }

    @FXML
    private void setAutoBackupOnStop(){
        SettingsEngine.getInstance().getSettings().autoBackupOnStop = autoBackupOnStop.isSelected();
    }

    @FXML
    private void setAutoBackupPath(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Выбор пути сохранения резервных копий");

        File file = directoryChooser.showDialog(null);
        if(file!=null){
            autoBackupPath.setText(file.getPath());
        }

        SettingsEngine.getInstance().getSettings().autoBackupPath = autoBackupPath.getText();
    }

    private void loadSettings(SettingsModel settings){
        productsInStock.setSelected(settings.productsInStockEnabled);
        invoiceFromStock.setSelected(settings.invoicesFromStock);
        sellsFromStock.setSelected(settings.sellsFromStock);

        autoVat.setSelected(settings.autoVat);
        autoExtraPrice.setSelected(settings.autoExtraPrice);

        autoBackupEnabled.setSelected(settings.autoBackupEnabled);
        autoBackupOnStart.setSelected(settings.autoBackupOnStart);
        autoBackupOnStop.setSelected(settings.autoBackupOnStop);
        autoBackupPath.setText(settings.autoBackupPath);
    }

    private void openSettings(boolean isProductsInStockEnabled, boolean isAutoBackupEnabled){
        if(isProductsInStockEnabled){
            invoiceFromStock.setDisable(false);
            sellsFromStock.setDisable(false);
        }else{
            invoiceFromStock.setDisable(true);
            sellsFromStock.setDisable(true);
        }

        if(isAutoBackupEnabled){
            autoBackupOnStart.setDisable(false);
            autoBackupOnStop.setDisable(false);
            autoBackupPath.setEditable(true);
            autoBackupPathButton.setDisable(false);
        }else{
            autoBackupOnStart.setDisable(true);
            autoBackupOnStop.setDisable(true);
            autoBackupPath.setEditable(false);
            autoBackupPathButton.setDisable(true);
        }
    }
}
