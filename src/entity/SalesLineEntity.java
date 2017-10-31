package entity;

import javax.persistence.*;

@Entity
@Table(name = "sales_line", schema = "minimag")
public class SalesLineEntity {
    private int id;
    private String salesNumber;
    private Integer itemId;
    private String itemName;
    private Integer count;
    private Double itemPrice;
    private Double fullLinePrice;

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
    @Column(name = "sales_number", nullable = true, length = 45)
    public String getSalesNumber() {
        return salesNumber;
    }

    public void setSalesNumber(String salesNumber) {
        this.salesNumber = salesNumber;
    }

    @Basic
    @Column(name = "item_id", nullable = true)
    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    @Basic
    @Column(name = "item_name", nullable = true, length = 45)
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Basic
    @Column(name = "count", nullable = true)
    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Basic
    @Column(name = "item_price", nullable = true, precision = 0)
    public Double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }

    @Basic
    @Column(name = "full_line_price", nullable = true, precision = 0)
    public Double getFullLinePrice() {
        return fullLinePrice;
    }

    public void setFullLinePrice(Double fullLinePrice) {
        this.fullLinePrice = fullLinePrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SalesLineEntity that = (SalesLineEntity) o;

        if (id != that.id) return false;
        if (salesNumber != null ? !salesNumber.equals(that.salesNumber) : that.salesNumber != null) return false;
        if (itemId != null ? !itemId.equals(that.itemId) : that.itemId != null) return false;
        if (itemName != null ? !itemName.equals(that.itemName) : that.itemName != null) return false;
        if (count != null ? !count.equals(that.count) : that.count != null) return false;
        if (itemPrice != null ? !itemPrice.equals(that.itemPrice) : that.itemPrice != null) return false;
        if (fullLinePrice != null ? !fullLinePrice.equals(that.fullLinePrice) : that.fullLinePrice != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (salesNumber != null ? salesNumber.hashCode() : 0);
        result = 31 * result + (itemId != null ? itemId.hashCode() : 0);
        result = 31 * result + (itemName != null ? itemName.hashCode() : 0);
        result = 31 * result + (count != null ? count.hashCode() : 0);
        result = 31 * result + (itemPrice != null ? itemPrice.hashCode() : 0);
        result = 31 * result + (fullLinePrice != null ? fullLinePrice.hashCode() : 0);
        return result;
    }
}
