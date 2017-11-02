package entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "prices", schema = "minimag")
public class PricesEntity {
    private int id;
    private String price;
    private int itemId;
    private Timestamp lastcreated;
    private String reason;

    public PricesEntity() {
    }

    public PricesEntity(int id, String price, int itemId, Timestamp lastcreated, String reason) {
        this.id = id;
        this.price = price;
        this.itemId = itemId;
        this.lastcreated = lastcreated;
        this.reason = reason;
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
    @Column(name = "price", nullable = false, length = 45)
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Basic
    @Column(name = "item_id", nullable = false)
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    @Basic
    @Column(name = "lastcreated", nullable = false)
    public Timestamp getLastcreated() {
        return lastcreated;
    }

    public void setLastcreated(Timestamp lastcreated) {
        this.lastcreated = lastcreated;
    }

    @Basic
    @Column(name = "reason", nullable = false, length = 45)
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PricesEntity that = (PricesEntity) o;

        if (id != that.id) return false;
        if (itemId != that.itemId) return false;
        if (price != null ? !price.equals(that.price) : that.price != null) return false;
        if (lastcreated != null ? !lastcreated.equals(that.lastcreated) : that.lastcreated != null) return false;
        if (reason != null ? !reason.equals(that.reason) : that.reason != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + itemId;
        result = 31 * result + (lastcreated != null ? lastcreated.hashCode() : 0);
        result = 31 * result + (reason != null ? reason.hashCode() : 0);
        return result;
    }
}
