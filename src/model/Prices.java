package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Prices {
	
	private final IntegerProperty id;
	private final StringProperty price;
	private final IntegerProperty itemId;
	
	public Prices() {
		this(null, null, null);
	}
	
	public Prices(Integer id, String price, Integer itemId) {
		this.id = new SimpleIntegerProperty(id);
		this.price = new SimpleStringProperty(price);
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
    
    public String getPrice() {
        return price.get();
    }

    public void setPrice(String price) {
        this.price.set(price);
    }

    public StringProperty priceProperty() {
        return price;
    }
}
