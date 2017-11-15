package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public enum SaleTypes {

    PURCHASE("покупка"),
    RETURN("возврат");

    private final String type;

    SaleTypes(final String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

    public static ObservableList<String> getTypes() {
        SaleTypes[] states = values();
        ObservableList<String> names = FXCollections.observableArrayList();

        for (SaleTypes state : states) {
            names.add(state.toString());
        }

        return names;
    }
}
