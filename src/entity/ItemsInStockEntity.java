package entity;

import javax.persistence.*;

@Entity
@Table(name = "items_in_stock", schema = "minimag")
public class ItemsInStockEntity {
    private int id;
    private String date;

    public ItemsInStockEntity() {
    }

    public ItemsInStockEntity(int id, String date) {
        this.id = id;
        this.date = date;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "date", nullable = false)
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
