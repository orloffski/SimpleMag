package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.List;

public class Counterparties {

	private final IntegerProperty id;
	private final StringProperty name;
	private final StringProperty adress;
	private final StringProperty unn;
	
	public Counterparties() {
		this(null, null, null, null);
	}
	
	public Counterparties(Integer id, String name, String adress, String unn) {
		this.id = new SimpleIntegerProperty(id);
		this.name = new SimpleStringProperty(name);
		this.adress = new SimpleStringProperty(adress);
		this.unn = new SimpleStringProperty(unn);
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
    
    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }
    
    public String getAdress() {
        return adress.get();
    }

    public void setAdress(String adress) {
        this.adress.set(adress);
    }

    public StringProperty adressProperty() {
        return adress;
    }
    
    public String getUnn() {
        return unn.get();
    }

    public void setUnn(String unn) {
        this.unn.set(unn);
    }

    public StringProperty unnProperty() {
        return unn;
    }

    public static int getCounterpartyIdByName(List<Counterparties> list, String name){
	    for(Counterparties item : list)
	        if(item.getName().equals(name))
	            return item.getId();

	    return -1;
    }
}
