package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ItemsInStock {

    private final IntegerProperty id;
    private final IntegerProperty idCounterparty;
    private final StringProperty date;
    private final StringProperty summ;

    public ItemsInStock(){this(null, null, null, null);}

    public ItemsInStock(Integer id, Integer idCounterparty, String date, String summ){
        this.id = new SimpleIntegerProperty(id);
        this.idCounterparty = new SimpleIntegerProperty(idCounterparty);
        this.date = new SimpleStringProperty(date);
        this.summ = new SimpleStringProperty(summ);
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

    public int getIdCounterparty() {
        return idCounterparty.get();
    }

    public void setIdCounterparty(int idCounterparty) {
        this.idCounterparty.set(idCounterparty);
    }

    public IntegerProperty idCounterpartyProperty() {
        return idCounterparty;
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

    public String getSumm() {
        return summ.get();
    }

    public void setSumm(String summ) {
        this.summ.set(summ);
    }

    public StringProperty summProperty() {
        return summ;
    }
}
