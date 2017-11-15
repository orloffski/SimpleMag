package model;

import entity.SalesHeaderEntity;
import javafx.beans.property.*;

public class SalesHeader {

    private final IntegerProperty id;
    private final StringProperty salesNumber;
    private final DoubleProperty cash;
    private final DoubleProperty nonCash;
    private final StringProperty salesType;
    private final StringProperty paymentType;
    private final StringProperty createUpdate;
    private final DoubleProperty fullSumm;

    public SalesHeader() {
        this(null, null, null, null, null, null, null);
    }

    public SalesHeader(Integer id, String salesNumber, Double cash, Double nonCash, String salesType, String paymentType, String createUpdate) {
        this.id = new SimpleIntegerProperty(id);
        this.salesNumber = new SimpleStringProperty(salesNumber);
        this.cash = new SimpleDoubleProperty(cash);
        this.nonCash = new SimpleDoubleProperty(nonCash);
        this.salesType = new SimpleStringProperty(salesType);
        this.paymentType = new SimpleStringProperty(paymentType);
        this.createUpdate = new SimpleStringProperty(createUpdate);

        this.fullSumm = null;
        this.setFullSumm(this.cash.asObject().get() + this.nonCash.get());
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

    public double getCash() {
        return cash.get();
    }

    public DoubleProperty cashProperty() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash.set(cash);
    }

    public double getNonCash() {
        return nonCash.get();
    }

    public DoubleProperty nonCashProperty() {
        return nonCash;
    }

    public void setNonCash(double nonCash) {
        this.nonCash.set(nonCash);
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

    public double getFullSumm() {
        return fullSumm.get();
    }

    public DoubleProperty fullSummProperty() {
        return fullSumm;
    }

    public void setFullSumm(double fullSumm) {
        this.fullSumm.set(fullSumm);
    }

    public static SalesHeader createHeaderFromEntity(SalesHeaderEntity saleHeader){
        return new SalesHeader(
                saleHeader.getId(),
                saleHeader.getSalesNumber(),
                saleHeader.getCash(),
                saleHeader.getNonCash(),
                saleHeader.getSalesType(),
                saleHeader.getPayment(),
                saleHeader.getLastcreateupdate().toString()
        );
    }
}
