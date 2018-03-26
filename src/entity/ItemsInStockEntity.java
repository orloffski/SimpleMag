package entity;

import javax.persistence.*;

@Entity
@Table(name = "items_in_stock", schema = "minimag")
public class ItemsInStockEntity {
    private String date;

    public ItemsInStockEntity() {
    }

    public ItemsInStockEntity(String date) {
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
