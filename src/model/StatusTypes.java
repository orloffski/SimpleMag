package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public enum StatusTypes {

	ENTERED("Проведен"),
	NOENTERED("Не проведен");
	
private final String type;
	
	StatusTypes(final String type) {
        this.type = type;
    }
	
	@Override
    public String toString() {
        return type;
    }
	
	public static ObservableList<String> getTypes() {
		StatusTypes[] states = values();
		ObservableList<String> names = FXCollections.observableArrayList();

		for (StatusTypes state : states) {
			names.add(state.toString());
		}

	    return names;
	}
}
