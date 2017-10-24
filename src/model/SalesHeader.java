package model;

import javafx.beans.property.*;

public class SalesHeader {

    private final IntegerProperty id;
    private final StringProperty salesNumber;
    private final DoubleProperty salesSumm;
    private final StringProperty salesType;
    private final StringProperty paymentType;
    private final StringProperty createUpdate;

    public SalesHeader() {
        this(null, null, null, null, null, null);
    }

    public SalesHeader(Integer id, String salesNumber, Double salesSumm, String salesType, String paymentType, String createUpdate) {
        this.id = new SimpleIntegerProperty(id);
        this.salesNumber = new SimpleStringProperty(salesNumber);
        this.salesSumm = new SimpleDoubleProperty(salesSumm);
        this.salesType = new SimpleStringProperty(salesType);
        this.paymentType = new SimpleStringProperty(paymentType);
        this.createUpdate = new SimpleStringProperty(createUpdate);
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

    public double getSalesSumm() {
        return salesSumm.get();
    }

    public DoubleProperty salesSummProperty() {
        return salesSumm;
    }

    public void setSalesSumm(double salesSumm) {
        this.salesSumm.set(salesSumm);
    }

    public String getSalesType() {
        return salesType.get();
    }

    public StringProperty salesTypeProperty() {
        return salesType;
    }

    public void setSalesType(String salesType) {
        this.salesType.set(salesType);
    }

    public String getPaymentType() {
        return paymentType.get();
    }

    public StringProperty paymentTypeProperty() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType.set(paymentType);
    }

    public String getCreateUpdate() {
        return createUpdate.get();
    }

    public StringProperty createUpdateProperty() {
        return createUpdate;
    }

    public void setCreateUpdate(String createUpdate) {
        this.createUpdate.set(createUpdate);
    }
}
