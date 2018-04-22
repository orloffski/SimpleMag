package model;

import javafx.beans.property.*;

public class BarcodeItemsFromStock {

    private final StringProperty barcode;
    private final StringProperty itemName;
    private final DoubleProperty itemCount;
    private final StringProperty expireDate;
    private final StringProperty invoiceNum;
    private final StringProperty price;
    private final IntegerProperty itemId;

    public BarcodeItemsFromStock() {
        this(null, null, null, null, null, null, null);
    }

    public BarcodeItemsFromStock(String barcode, String itemName, Double itemCount, String expireDate, String invoiceNum, Integer itemId, String price) {
        this.barcode = new SimpleStringProperty(barcode);
        this.itemName = new SimpleStringProperty(itemName);
        this.itemCount = new SimpleDoubleProperty(itemCount);
        this.expireDate = new SimpleStringProperty(expireDate);
        this.invoiceNum = new SimpleStringProperty(invoiceNum);
        this.itemId = new SimpleIntegerProperty(itemId);
        this.price = new SimpleStringProperty(price);
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

    public Double getItemCount() {
        return itemCount.get();
    }

    public DoubleProperty itemCountProperty() {
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

    public String getPrice() {
        return price.get();
    }

    public StringProperty priceProperty() {
        return price;
    }

    public void setPrice(String price) {
        this.price.set(price);
    }
}
