package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Items {
	
	private final IntegerProperty id;
	private final StringProperty vendorCode;
	private final StringProperty name;
	
	public Items() {
		this(null, null, null);
	}
	
	public Items(Integer id, String vendorCode, String name) {
		this.id = new SimpleIntegerProperty(id);
		this.vendorCode = new SimpleStringProperty(vendorCode);
		this.name = new SimpleStringProperty(name);
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
    
    public String getVendorCode() {
        return vendorCode.get();
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode.set(vendorCode);
    }

    public StringProperty vendorCodeProperty() {
        return vendorCode;
    }
    
    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

}
