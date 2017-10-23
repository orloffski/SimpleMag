package model;

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
	private final IntegerProperty vat;
	private final IntegerProperty extraPrice;
	private final StringProperty invoiceNumber;
	private final StringProperty itemName;
	private final DoubleProperty vendorPrice;
	private final DoubleProperty retailPrice;
	private final IntegerProperty count;
	private final DoubleProperty summVat;
	private final DoubleProperty summIncludeVat;

	private boolean toDelete;
	private boolean toUpdate;

	public InvoiceLine() {
		this(null, null, null, null, null, null, null, null, null, null, null, null);
	}
	
	public InvoiceLine(Integer id, Integer lineNumber, String invoiceNumber, Integer itemId, Double vendorPrice, Integer vat, Integer extraPrice, Double retailPrice, String itemName, Integer count, Double summVat, Double summIncludeVat) {
		this.id = new SimpleIntegerProperty(id);
		this.lineNumber = new SimpleIntegerProperty(lineNumber);
		this.itemId = new SimpleIntegerProperty(itemId);
		this.vat = new SimpleIntegerProperty(vat);
		this.extraPrice = new SimpleIntegerProperty(extraPrice);
		this.invoiceNumber = new SimpleStringProperty(invoiceNumber);
		this.itemName = new SimpleStringProperty(itemName);
		this.vendorPrice = new SimpleDoubleProperty(vendorPrice);
		this.retailPrice = new SimpleDoubleProperty(retailPrice);
		this.count = new SimpleIntegerProperty(count);
		this.summVat = new SimpleDoubleProperty(summVat);
		this.summIncludeVat = new SimpleDoubleProperty(summIncludeVat);
	}

    public boolean isToDelete() {
        return toDelete;
    }

    public void setToDelete(boolean toDelete) {
        this.toDelete = toDelete;
    }

    public boolean isToUpdate() {
        return toUpdate;
    }

    public void setToUpdate(boolean toUpdate) {
        this.toUpdate = toUpdate;
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

    public int getCount() {
        return count.get();
    }

    public void setCount(int count) {
        this.count.set(count);
    }

    public IntegerProperty countProperty() {
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
    
    public int getVat() {
        return vat.get();
    }

    public void setVat(int vat) {
        this.vat.set(vat);
    }

    public IntegerProperty vatProperty() {
        return vat;
    }
    
    public int getExtraPrice() {
        return extraPrice.get();
    }

    public void setExtraPrice(int extraPrice) {
        this.extraPrice.set(extraPrice);
    }

    public IntegerProperty extraPriceProperty() {
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


}
