package entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sales_reports", schema = "minimag")
public class SalesReportsEntity {
    private String date;

    public SalesReportsEntity() {
    }

    public SalesReportsEntity(String date) {
        this.date = date;
    }

    @Id
    @Column(name = "date", nullable = false)
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
