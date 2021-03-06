package entity;

import model.SalesLine;

import javax.persistence.*;

@Entity
@Table(name = "sales_line", schema = "minimag")
public class SalesLineEntity {
    private int id;
    private String salesNumber;
    private Integer itemId;
    private String itemName;
    private Double count;
    private Double itemPrice;
    private Double fullLinePrice;
    private Integer counterpartyId;

    public SalesLineEntity() {
    }

    public SalesLineEntity(int id, String salesNumber, Integer itemId, String itemName, Double count, Double itemPrice, Double fullLinePrice, Integer counterpartyId) {
        this.id = id;
        this.salesNumber = salesNumber;
        this.itemId = itemId;
        this.itemName = itemName;
        this.count = count;
        this.itemPrice = itemPrice;
        this.fullLinePrice = fullLinePrice;
        this.counterpartyId = counterpartyId;
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
    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
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

    @Basic
    @Column(name = "counterparty_id", nullable = true)
    public Integer getCounterpartyId() {
        return counterpartyId;
    }

    public void setCounterpartyId(Integer counterpartyId) {
        this.counterpartyId = counterpartyId;
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
        if (counterpartyId != null ? !counterpartyId.equals(that.counterpartyId) : that.counterpartyId != null) return false;

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
        result = 31 * result + (counterpartyId != null ? counterpartyId.hashCode() : 0);
        return result;
    }

    public static SalesLineEntity createSalesLineEntityFromSalesLine(SalesLine line){
        return new SalesLineEntity(
                line.getId(),
                line.getSalesNumber(),
                line.getItemId(),
                line.getItemName(),
                line.getCount(),
                line.getItemPrice(),
                line.getLinePrice(),
                line.getCounterpartyId()
        );
    }
}
