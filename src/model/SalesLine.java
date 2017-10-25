package model;

import javafx.beans.property.*;

public class SalesLine {

    private final IntegerProperty id;
    private final StringProperty salesNumber;
    private final IntegerProperty itemId;
    private final StringProperty itemName;
    private final IntegerProperty count;
    private final DoubleProperty itemPrice;
    private final DoubleProperty linePrice;

    public SalesLine(){
        this(null, null, null, null, null, null, null);
    }

    public SalesLine(Integer id, String salesNumber, Integer itemId, String itemName, Integer count, Double itemPrice, Double linePrice){
        this.id = new SimpleIntegerProperty(id);
        this.salesNumber = new SimpleStringProperty(salesNumber);
        this.itemId = new SimpleIntegerProperty(itemId);
        this.itemName = new SimpleStringProperty(itemName);
        this.count = new SimpleIntegerProperty(count);
        this.itemPrice = new SimpleDoubleProperty(itemPrice);
        this.linePrice = new SimpleDoubleProperty(linePrice);
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

    public String getSalesNumber() {
        return salesNumber.get();
    }

    public StringProperty salesNumberProperty() {
        return salesNumber;
    }

    public void setSalesNumber(String salesNumber) {
        this.salesNumber.set(salesNumber);
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

    public int getCount() {
        return count.get();
    }

    public IntegerProperty countProperty() {
        return count;
    }

    public void setCount(int count) {
        this.count.set(count);
    }

    public double getItemPrice() {
        return itemPrice.get();
    }

    public DoubleProperty itemPriceProperty() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice.set(itemPrice);
    }

    public double getLinePrice() {
        return linePrice.get();
    }

    public DoubleProperty linePriceProperty() {
        return linePrice;
    }

    public void setLinePrice(double linePrice) {
        this.linePrice.set(linePrice);
    }
}