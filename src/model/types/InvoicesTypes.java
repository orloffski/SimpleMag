package model.types;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public enum InvoicesTypes {
	RECEIPT("Поступление"),
	RETURN("Возврат"),
	DELIVERY("Перемещение"),
	INITIAL("Ввод начальных остатков");
	
	private final String type;
	
	InvoicesTypes(final String type) {
        this.type = type;
    }
	
	@Override
    public String toString() {
        return type;
    }
	
	public static ObservableList<String> getTypes() {
		InvoicesTypes[] states = values();
		ObservableList<String> names = FXCollections.observableArrayList();

		for (InvoicesTypes state : states) {
			names.add(state.toString());
		}

	    return names;
	}
}
