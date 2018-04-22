package model;

import entity.InvoicesLinesEntity;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class InvoiceLine {

	private final IntegerProperty id;
	private final IntegerProperty lineNumber;
	private final IntegerProperty itemId;
	private final DoubleProperty vat;
	private final DoubleProperty extraPrice;
	private final StringProperty invoiceNumber;
	private final StringProperty itemName;
	private final DoubleProperty vendorPrice;
	private final DoubleProperty retailPrice;
	private final DoubleProperty count;
	private final DoubleProperty summVat;
	private final DoubleProperty summIncludeVat;
    private final StringProperty expireDate;

	public InvoiceLine() {
		this(null, null, null, null, null, null, null, null, null, null, null, null, null);
	}
	
	public InvoiceLine(Integer id, Integer lineNumber, String invoiceNumber, Integer itemId, Double vendorPrice, Double vat, Double extraPrice, Double retailPrice, String itemName, Double count, Double summVat, Double summIncludeVat, String expareDate) {
		this.id = new SimpleIntegerProperty(id);
		this.lineNumber = new SimpleIntegerProperty(lineNumber);
		this.itemId = new SimpleIntegerProperty(itemId);
		this.vat = new SimpleDoubleProperty(vat);
		this.extraPrice = new SimpleDoubleProperty(extraPrice);
		this.invoiceNumber = new SimpleStringProperty(invoiceNumber);
		this.itemName = new SimpleStringProperty(itemName);
		this.vendorPrice = new SimpleDoubleProperty(vendorPrice);
		this.retailPrice = new SimpleDoubleProperty(retailPrice);
		this.count = new SimpleDoubleProperty(count);
		this.summVat = new SimpleDoubleProperty(summVat);
		this.summIncludeVat = new SimpleDoubleProperty(summIncludeVat);
		this.expireDate = new SimpleStringProperty(expareDate);
	}

    public double getSummIncludeVat() {
        return summIncludeVat.get();
    }

    public DoubleProperty summIncludeVatProperty() {
        return summIncludeVat;
    }

    public void setSummIncludeVat(double summIncludeVat) {
        this.summIncludeVat.set(summIncludeVat);
    }

    public double getSummVat() {
        return summVat.get();
    }

    public DoubleProperty summVatProperty() {
        return summVat;
    }

    public void setSummVat(double summVat) {
        this.summVat.set(summVat);
    }

    public double getCount() {
        return count.get();
    }

    public void setCount(double count) {
        this.count.set(count);
    }

    public DoubleProperty countProperty() {
        return count;
    }
    
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }
    
    public int getLineNumber() {
        return lineNumber.get();
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber.set(lineNumber);
    }

    public IntegerProperty lineNumberProperty() {
        return lineNumber;
    }
    
    public int getItemId() {
        return itemId.get();
    }

    public void setItemId(int itemId) {
        this.itemId.set(itemId);
    }

    public IntegerProperty itemIdProperty() {
        return itemId;
    }
    
    public Double getVat() {
        return vat.get();
    }

    public void setVat(Double vat) {
        this.vat.set(vat);
    }

    public DoubleProperty vatProperty() {
        return vat;
    }
    
    public Double getExtraPrice() {
        return extraPrice.get();
    }

    public void setExtraPrice(double extraPrice) {
        this.extraPrice.set(extraPrice);
    }

    public DoubleProperty extraPriceProperty() {
        return extraPrice;
    }
    
    public String getInvoiceNumber() {
        return invoiceNumber.get();
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber.set(invoiceNumber);
    }

    public StringProperty invoiceNumberProperty() {
        return invoiceNumber;
    }
    
    public String getItemName() {
        return itemName.get();
    }

    public void setItemName(String itemName) {
        this.itemName.set(itemName);
    }

    public StringProperty itemNameProperty() {
        return itemName;
    }
    
    public double getVendorPrice() {
        return vendorPrice.get();
    }

    public void setVendorPrice(double vendorPrice) {
        this.vendorPrice.set(vendorPrice);
    }

    public DoubleProperty vendorPriceProperty() {
        return vendorPrice;
    }
    
    public double getRetailPrice() {
        return retailPrice.get();
    }

    public void setRetailPrice(double retailPrice) {
        this.retailPrice.set(retailPrice);
    }

    public DoubleProperty retailPriceProperty() {
        return retailPrice;
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

    public static InvoiceLine createInvoiceLineFromInvoiceLineEntity(InvoicesLinesEntity line){
	    return new InvoiceLine(
	            line.getId(),
                line.getLineNumber(),
                line.getInvoiceNumber(),
                line.getItemId(),
                line.getVendorPrice(),
                line.getVat().doubleValue(),
                line.getExtraPrice(),
                line.getRetailPrice(),
                line.getItemName(),
                line.getCount(),
                line.getSummVat(),
                line.getSummInclVat(),
                line.getExpireDate()
        );
    }

}
