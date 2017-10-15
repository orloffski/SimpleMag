package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Units {
	private final IntegerProperty id;
	private final StringProperty unit;
	
	public Units() {
		this(null, null);
	}
	
	public Units(Integer id, String unit) {
		this.id = new SimpleIntegerProperty(id);
		this.unit = new SimpleStringProperty(unit);
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
    
    public String getUnit() {
        return unit.get();
    }

    public void setUnit(String unit) {
        this.unit.set(unit);
    }

    public StringProperty unitProperty() {
        return unit;
    }
}
