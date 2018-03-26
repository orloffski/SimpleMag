package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ItemsInStock {

    private final IntegerProperty id;
    private final StringProperty date;

    public ItemsInStock(){this(null, null);}

    public ItemsInStock(Integer id,String date){
        this.id = new SimpleIntegerProperty(id);
        this.date = new SimpleStringProperty(date);
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

    public String getDate() {
        return date.get();
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public StringProperty dateProperty() {
        return date;
    }
}
