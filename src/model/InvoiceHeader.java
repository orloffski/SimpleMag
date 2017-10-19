package model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class InvoiceHeader {

	private final IntegerProperty id;
	private final StringProperty number;
	private final StringProperty type;
	private final StringProperty status;
	private final StringProperty counterparty;
	private final IntegerProperty count;
	private final DoubleProperty summ;
	private final IntegerProperty counterpartyId;
	private final StringProperty lastcreated;
	private final IntegerProperty recipientId;
	private final StringProperty recipientName;
	
	public InvoiceHeader() {
		this(null, null, null, null, null, null, null, null, null, null, null);
	}
	
	public InvoiceHeader(Integer id, String number, String type, String status, String counterparty, Integer count, Double summ, Integer counterpartyId, String lastcreated, Integer recipientId, String recipientName) {
		this.id = new SimpleIntegerProperty(id);
		this.number = new SimpleStringProperty(number);
		this.type = new SimpleStringProperty(type);
		this.status = new SimpleStringProperty(status);
		this.counterparty = new SimpleStringProperty(counterparty);
		this.count = new SimpleIntegerProperty(count);
		this.summ = new SimpleDoubleProperty(summ);
		this.counterpartyId = new SimpleIntegerProperty(counterpartyId);
		this.lastcreated = new SimpleStringProperty(lastcreated);
		this.recipientId = new SimpleIntegerProperty(recipientId);
		this.recipientName = new SimpleStringProperty(recipientName);
	}
	
	public int getRecipientd() {
        return recipientId.get();
    }

    public void setRecipientId(int recipientId) {
        this.recipientId.set(recipientId);
    }

    public IntegerProperty recipientIdProperty() {
        return recipientId;
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
    
    public int getCount() {
        return count.get();
    }

    public void setCount(int count) {
        this.count.set(count);
    }

    public IntegerProperty countProperty() {
        return count;
    }
    
    public int getCounterpartyId() {
        return counterpartyId.get();
    }

    public void setCounterpartyId(int counterpartyId) {
        this.counterpartyId.set(counterpartyId);
    }

    public IntegerProperty counterpartyIdProperty() {
        return counterpartyId;
    }
    
    public double getSumm() {
        return summ.get();
    }

    public void setSumm(double summ) {
        this.summ.set(summ);
    }

    public DoubleProperty summProperty() {
        return summ;
    }
    
    public String getNumber() {
        return number.get();
    }

    public void setNumber(String number) {
        this.number.set(number);
    }

    public StringProperty numberProperty() {
        return number;
    }
    
    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public StringProperty typeProperty() {
        return type;
    }
    
    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public StringProperty statusProperty() {
        return status;
    }
    
    public String getCounterparty() {
        return status.get();
    }

    public void setCounterparty(String counterparty) {
        this.counterparty.set(counterparty);
    }

    public StringProperty counterpartyProperty() {
        return counterparty;
    }
    
    public String getLastcreated() {
        return lastcreated.get();
    }

    public void setLastcreated(String lastcreated) {
        this.lastcreated.set(lastcreated);
    }

    public StringProperty lastcreatedProperty() {
        return lastcreated;
    }
    
    public String getRecipientName() {
        return recipientName.get();
    }

    public void setRecipientName(String recipientName) {
        this.recipientName.set(recipientName);
    }

    public StringProperty recipientNameProperty() {
        return recipientName;
    }
}
