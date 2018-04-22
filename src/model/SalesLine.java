package model;

import entity.SalesLineEntity;
import javafx.beans.property.*;

public class SalesLine {

    private final IntegerProperty id;
    private final StringProperty salesNumber;
    private final IntegerProperty itemId;
    private final StringProperty itemName;
    private final DoubleProperty count;
    private final DoubleProperty itemPrice;
    private final DoubleProperty linePrice;
    private final IntegerProperty counterpartyId;

    public SalesLine(){
        this(null, null, null, null, null, null, null, null);
    }

    public SalesLine(Integer id, String salesNumber, Integer itemId, String itemName, Double count, Double itemPrice, Double linePrice, Integer counterpartyId){
        this.id = new SimpleIntegerProperty(id);
        this.salesNumber = new SimpleStringProperty(salesNumber);
        this.itemId = new SimpleIntegerProperty(itemId);
        this.itemName = new SimpleStringProperty(itemName);
        this.count = new SimpleDoubleProperty(count);
        this.itemPrice = new SimpleDoubleProperty(itemPrice);
        this.linePrice = new SimpleDoubleProperty(linePrice);
        this.counterpartyId = new SimpleIntegerProperty(counterpartyId);
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

    public Double getCount() {
        return count.get();
    }

    public DoubleProperty countProperty() {
        return count;
    }

    public void setCount(double count) {
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

    public static SalesLine createSalesLineFromSalesLineEntity(SalesLineEntity entity){
        return new SalesLine(
                entity.getId(),
                entity.getSalesNumber(),
                entity.getItemId(),
                entity.getItemName(),
                entity.getCount(),
                entity.getItemPrice(),
                entity.getFullLinePrice(),
                entity.getCounterpartyId()
        );
    }
}
