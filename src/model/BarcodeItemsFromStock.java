package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BarcodeItemsFromStock {

    private final StringProperty barcode;
    private final StringProperty itemName;
    private final IntegerProperty itemCount;
    private final StringProperty expireDate;
    private final StringProperty invoiceNum;
    private final IntegerProperty itemId;

    public BarcodeItemsFromStock() {
        this(null, null, null, null, null, null);
    }

    public BarcodeItemsFromStock(String barcode, String itemName, Integer itemCount, String expireDate, String invoiceNum, Integer itemId) {
        this.barcode = new SimpleStringProperty(barcode);
        this.itemName = new SimpleStringProperty(itemName);
        this.itemCount = new SimpleIntegerProperty(itemCount);
        this.expireDate = new SimpleStringProperty(expireDate);
        this.invoiceNum = new SimpleStringProperty(invoiceNum);
        this.itemId = new SimpleIntegerProperty(itemId);
    }

    public String getBarcode() {
        return barcode.get();
    }

    public StringProperty barcodeProperty() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode.set(barcode);
    }

    public String getItemName() {
        return itemName.get();
    }

    public StringProperty itemNameProperty() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName.set(itemName);
    }

    public int getItemCount() {
        return itemCount.get();
    }

    public IntegerProperty itemCountProperty() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount.set(itemCount);
    }

    public String getExpireDate() {
        return expireDate.get();
    }

    public StringProperty expireDateProperty() {
        return expireDate;
    }

    public void setExpireDate(String expareDate) {
        this.expireDate.set(expareDate);
    }

    public String getInvoiceNum() {
        return invoiceNum.get();
    }

    public StringProperty invoiceNumProperty() {
        return invoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum.set(invoiceNum);
    }

    public int getItemId() {
        return itemId.get();
    }

    public IntegerProperty itemIdProperty() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId.set(itemId);
    }
}
