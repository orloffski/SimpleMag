package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BarcodesItems {

    private final StringProperty barcode;
    private final StringProperty vendorCode;
    private final StringProperty name;
    private final IntegerProperty itemId;

    public BarcodesItems(){
        this(null, null, null, null);
    }

    public BarcodesItems(String barcode, String vendorCode, String name, Integer itemId){
        this.barcode = new SimpleStringProperty(barcode);
        this.vendorCode = new SimpleStringProperty(vendorCode);
        this.name = new SimpleStringProperty(name);
        this.itemId = new SimpleIntegerProperty(itemId);
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

    public String getBarcode() {
        return barcode.get();
    }

    public StringProperty barcodeProperty() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode.set(barcode);
    }

    public String getVendorCode() {
        return vendorCode.get();
    }

    public StringProperty vendorCodeProperty() {
        return vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode.set(vendorCode);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }
}
