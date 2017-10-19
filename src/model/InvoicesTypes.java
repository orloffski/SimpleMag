package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public enum InvoicesTypes {
	RECEIPT("�����������"),
	RETURN("�������"),
	DELIVERY("�����������"),
	INITIAL("���� ��������� ��������");
	
	private final String type;
	
	private InvoicesTypes(final String type) {
        this.type = type;
    }
	
	@Override
    public String toString() {
        return type;
    }
	
	public static ObservableList<String> getTypes() {
		InvoicesTypes[] states = values();
		ObservableList<String> names = FXCollections.observableArrayList();

	    for (int i = 0; i < states.length; i++) {
	    	names.add(states[i].toString());
	    }

	    return names;
	}
}
