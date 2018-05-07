package model.types;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public enum PaymentTypes {

    CASH("наличный"),
    NONCASH("безналичный"),
    COMBINECASH("сложная оплата");

    private final String type;

    PaymentTypes(final String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

    public static ObservableList<String> getTypes() {
        PaymentTypes[] states = values();
        ObservableList<String> names = FXCollections.observableArrayList();

        for (PaymentTypes state : states) {
            names.add(state.toString());
        }

        return names;
    }
}
