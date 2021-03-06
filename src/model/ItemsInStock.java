package model;

import entity.ItemsInStockEntity;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import utils.DateUtils;

public class ItemsInStock {

    private final StringProperty date;

    public ItemsInStock(){this(null);}

    public ItemsInStock(String date){
        this.date = new SimpleStringProperty(date);
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

    public static ItemsInStock createItemsInStockFromItemsInStockEntity(ItemsInStockEntity itemsInStockEntity){
        return new ItemsInStock(
                DateUtils.transformDateFromDB(
                        itemsInStockEntity.getDate()
                )
        );
    }
}
