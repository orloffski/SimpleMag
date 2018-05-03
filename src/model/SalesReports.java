package model;

import entity.SalesReportsEntity;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import utils.DateUtils;

public class SalesReports {

    private final StringProperty date;

    public SalesReports(){this(null);}

    public SalesReports(String date){
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

    public static SalesReports createSalesReportsFromSalesReportsEntity(SalesReportsEntity salesReportsEntity){
        return new SalesReports(
                DateUtils.transformDateFromDB(
                        salesReportsEntity.getDate()
                )
        );
    }
}
