package model;

import entity.ItemsEntity;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Items {
	
	private final IntegerProperty id;
	private final StringProperty vendorCode;
	private final StringProperty vendorCountry;
	private final StringProperty name;
	private final IntegerProperty unitId;
	
	public Items() {
		this(null, null, null, null, null);
	}
	
	public Items(Integer id, String vendorCode, String name, String vendorCountry, Integer unitId) {
		this.id = new SimpleIntegerProperty(id);
		this.vendorCode = new SimpleStringProperty(vendorCode);
		this.name = new SimpleStringProperty(name);
		this.vendorCountry = new SimpleStringProperty(vendorCountry);
		this.unitId = new SimpleIntegerProperty(unitId);
	}
	
	public int getUnitId() {
        return unitId.get();
    }

    public void setUnitId(int unitId) {
        this.unitId.set(unitId);
    }

    public IntegerProperty unitIdProperty() {
        return unitId;
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
    
    public String getVendorCountry() {
        return vendorCountry.get();
    }

    public void setVendorCountry(String vendorCountry) {
        this.vendorCountry.set(vendorCountry);
    }

    public StringProperty vendorCountryProperty() {
        return vendorCountry;
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

    public static Items createItemsFromItemsEntity(ItemsEntity itemsEntity){
	    return new Items(
	            itemsEntity.getId(),
                itemsEntity.getVendorCode(),
                itemsEntity.getName(),
                itemsEntity.getVendorCountry(),
                itemsEntity.getUnitId()
        );
    }

}
