package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Barcodes {

	private final IntegerProperty id;
	private final StringProperty barcode;
	private final IntegerProperty itemId;
	
	public Barcodes() {
		this(null, null, null);
	}
	
	public Barcodes(Integer id, String barcode, Integer itemId) {
		this.id = new SimpleIntegerProperty(id);
		this.barcode = new SimpleStringProperty(barcode);
		this.itemId = new SimpleIntegerProperty(itemId);
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
    
    public int getitemId() {
        return itemId.get();
    }

    public void setItemId(int itemId) {
        this.itemId.set(itemId);
    }

    public IntegerProperty itemIdProperty() {
        return itemId;
    }
    
    public String getBarcode() {
        return barcode.get();
    }

    public void setBarcode(String barcode) {
        this.barcode.set(barcode);
    }

    public StringProperty barcodeProperty() {
        return barcode;
    }
}
