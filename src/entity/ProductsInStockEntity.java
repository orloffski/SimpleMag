package entity;

import javax.persistence.*;

@Entity
@Table(name = "products_in_stock", schema = "minimag")
public class ProductsInStockEntity {
    private int id;
    private int itemId;
    private String itemName;
    private Double itemsCount;
    private String invoiceNumber;
    private String invoiceDate;
    private int counterpartyId;
    private String expireDate;

    public ProductsInStockEntity() {
    }

    public ProductsInStockEntity(int id, int item_id, String itemName, Double itemsCount, String invoiceNumber, String invoiceDate, int counterpartyId, String expireDate) {
        this.id = id;
        this.itemId = item_id;
        this.itemName = itemName;
        this.itemsCount = itemsCount;
        this.invoiceNumber = invoiceNumber;
        this.invoiceDate = invoiceDate;
        this.counterpartyId = counterpartyId;
        this.expireDate = expireDate;
    }

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    @Column(name = "item_name", nullable = true, length = 45)
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Basic
    @Column(name = "items_count", nullable = true, precision = 0)
    public Double getItemsCount() {
        return itemsCount;
    }

    public void setItemsCount(Double itemsCount) {
        this.itemsCount = itemsCount;
    }

    @Basic
    @Column(name = "invoice_number", nullable = true, length = 45)
    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    @Basic
    @Column(name = "invoice_date", nullable = true, length = 45)
    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    @Basic
    @Column(name = "counterparty_id", nullable = false)
    public int getCounterpartyId() {
        return counterpartyId;
    }

    public void setCounterpartyId(int counterpartyId) {
        this.counterpartyId = counterpartyId;
    }

    @Basic
    @Column(name = "expire_date", nullable = true, length = 45)
    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.invoiceDate = expireDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductsInStockEntity that = (ProductsInStockEntity) o;

        if (id != that.id) return false;
        if (itemId != that.itemId) return false;
        if (counterpartyId != that.counterpartyId) return false;
        if (itemName != null ? !itemName.equals(that.itemName) : that.itemName != null) return false;
        if (itemsCount != null ? !itemsCount.equals(that.itemsCount) : that.itemsCount != null) return false;
        if (invoiceNumber != null ? !invoiceNumber.equals(that.invoiceNumber) : that.invoiceNumber != null)
            return false;
        if (invoiceDate != null ? !invoiceDate.equals(that.invoiceDate) : that.invoiceDate != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + itemId;
        result = 31 * result + counterpartyId;
        result = 31 * result + (itemName != null ? itemName.hashCode() : 0);
        result = 31 * result + (itemsCount != null ? itemsCount.hashCode() : 0);
        result = 31 * result + (invoiceNumber != null ? invoiceNumber.hashCode() : 0);
        result = 31 * result + (invoiceDate != null ? invoiceDate.hashCode() : 0);
        return result;
    }

    public static ProductsInStockEntity createProductsInStockEntityFromInvoiceLineEntity(InvoicesLinesEntity line, String invoiceDate, int counterpartyId){
        return new ProductsInStockEntity(
                0,
                line.getItemId(),
                line.getItemName(),
                Double.valueOf(line.getCount()),
                line.getInvoiceNumber(),
                invoiceDate,
                counterpartyId,
                line.getExpireDate()
        );
    }
}
