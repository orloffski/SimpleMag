package model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class ProductsInStock {

    private final IntegerProperty id;
    private final IntegerProperty itemId;
    private final StringProperty itemName;
    private final DoubleProperty itemsCount;
    private final StringProperty invoiceNumber;
    private final StringProperty invoiceDate;
    private final IntegerProperty counterpartyId;
    private final StringProperty expireDate;


    public ProductsInStock() {
        this(null,null,null,null,null,null,null, null);
    }

    public ProductsInStock(IntegerProperty id, IntegerProperty itemId, StringProperty itemName, DoubleProperty itemsCount, StringProperty invoiceNumber, StringProperty invoiceDate, IntegerProperty counterpartyId, StringProperty expireDate) {
        this.id = id;
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemsCount = itemsCount;
        this.invoiceNumber = invoiceNumber;
        this.invoiceDate = invoiceDate;
        this.counterpartyId = counterpartyId;
        this.expireDate = expireDate;
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
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

    public String getItemName() {
        return itemName.get();
    }

    public StringProperty itemNameProperty() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName.set(itemName);
    }

    public double getItemsCount() {
        return itemsCount.get();
    }

    public DoubleProperty itemsCountProperty() {
        return itemsCount;
    }

    public void setItemsCount(double itemsCount) {
        this.itemsCount.set(itemsCount);
    }

    public String getInvoiceNumber() {
        return invoiceNumber.get();
    }

    public StringProperty invoiceNumberProperty() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber.set(invoiceNumber);
    }

    public String getInvoiceDate() {
        return invoiceDate.get();
    }

    public StringProperty invoiceDateProperty() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate.set(invoiceDate);
    }

    public int getCounterpartyId() {
        return counterpartyId.get();
    }

    public IntegerProperty counterpartyIdProperty() {
        return counterpartyId;
    }

    public void setCounterpartyId(int counterpartyId) {
        this.counterpartyId.set(counterpartyId);
    }

    public String getExpireDate() {
        return expireDate.get();
    }

    public StringProperty expireDateProperty() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate.set(expireDate);
    }
}
