package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public enum PrintForms {
    PRINT_FORMS("Печатные формы"),
    RETAIL_PRICE_REGISTER("Реестр цен"),
    PRICES("Ценники");

    private final String type;

    PrintForms(final String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

    public static ObservableList<String> getTypes() {
        PrintForms[] forms = values();
        ObservableList<String> names = FXCollections.observableArrayList();

        for (PrintForms form : forms) {
            names.add(form.toString());
        }

        return names;
    }
}
