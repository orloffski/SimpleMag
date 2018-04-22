package entity;

import model.InvoiceLine;

import javax.persistence.*;

@Entity
@Table(name = "invoices_lines", schema = "minimag")
public class InvoicesLinesEntity {
    private int id;
    private Integer lineNumber;
    private String invoiceNumber;
    private int itemId;
    private Double vendorPrice;
    private Byte vat;
    private Byte extraPrice;
    private Double retailPrice;
    private String itemName;
    private Double count;
    private Double summVat;
    private Double summInclVat;
    private String expireDate;

    public InvoicesLinesEntity() {
    }

    public InvoicesLinesEntity(int id, Integer lineNumber, String invoiceNumber, int itemId, Double vendorPrice, Byte vat, Byte extraPrice, Double retailPrice, String itemName, Double count, Double summVat, Double summInclVat, String expireDate) {
        this.id = id;
        this.lineNumber = lineNumber;
        this.invoiceNumber = invoiceNumber;
        this.itemId = itemId;
        this.vendorPrice = vendorPrice;
        this.vat = vat;
        this.extraPrice = extraPrice;
        this.retailPrice = retailPrice;
        this.itemName = itemName;
        this.count = count;
        this.summVat = summVat;
        this.summInclVat = summInclVat;
        this.expireDate = expireDate;
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
    @Column(name = "line_number", nullable = true)
    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
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
    @Column(name = "item_id", nullable = false)
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    @Basic
    @Column(name = "vendor_price", nullable = true, precision = 0)
    public Double getVendorPrice() {
        return vendorPrice;
    }

    public void setVendorPrice(Double vendorPrice) {
        this.vendorPrice = vendorPrice;
    }

    @Basic
    @Column(name = "vat", nullable = true)
    public Byte getVat() {
        return vat;
    }

    public void setVat(Byte vat) {
        this.vat = vat;
    }

    @Basic
    @Column(name = "extra_price", nullable = true)
    public Byte getExtraPrice() {
        return extraPrice;
    }

    public void setExtraPrice(Byte extraPrice) {
        this.extraPrice = extraPrice;
    }

    @Basic
    @Column(name = "retail_price", nullable = true, precision = 0)
    public Double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(Double retailPrice) {
        this.retailPrice = retailPrice;
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
    @Column(name = "summ_vat", nullable = true, precision = 0)
    public Double getSummVat() {
        return summVat;
    }

    public void setSummVat(Double summVat) {
        this.summVat = summVat;
    }

    @Basic
    @Column(name = "summ_incl_vat", nullable = true, precision = 0)
    public Double getSummInclVat() {
        return summInclVat;
    }

    public void setSummInclVat(Double summInclVat) {
        this.summInclVat = summInclVat;
    }

    @Basic
    @Column(name = "expire_date", nullable = true, length = 45)
    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InvoicesLinesEntity that = (InvoicesLinesEntity) o;

        if (id != that.id) return false;
        if (itemId != that.itemId) return false;
        if (lineNumber != null ? !lineNumber.equals(that.lineNumber) : that.lineNumber != null) return false;
        if (invoiceNumber != null ? !invoiceNumber.equals(that.invoiceNumber) : that.invoiceNumber != null)
            return false;
        if (vendorPrice != null ? !vendorPrice.equals(that.vendorPrice) : that.vendorPrice != null) return false;
        if (vat != null ? !vat.equals(that.vat) : that.vat != null) return false;
        if (extraPrice != null ? !extraPrice.equals(that.extraPrice) : that.extraPrice != null) return false;
        if (retailPrice != null ? !retailPrice.equals(that.retailPrice) : that.retailPrice != null) return false;
        if (itemName != null ? !itemName.equals(that.itemName) : that.itemName != null) return false;
        if (count != null ? !count.equals(that.count) : that.count != null) return false;
        if (summVat != null ? !summVat.equals(that.summVat) : that.summVat != null) return false;
        if (summInclVat != null ? !summInclVat.equals(that.summInclVat) : that.summInclVat != null) return false;
        if (expireDate != null ? !expireDate.equals(that.expireDate) : that.expireDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (lineNumber != null ? lineNumber.hashCode() : 0);
        result = 31 * result + (invoiceNumber != null ? invoiceNumber.hashCode() : 0);
        result = 31 * result + itemId;
        result = 31 * result + (vendorPrice != null ? vendorPrice.hashCode() : 0);
        result = 31 * result + (vat != null ? vat.hashCode() : 0);
        result = 31 * result + (extraPrice != null ? extraPrice.hashCode() : 0);
        result = 31 * result + (retailPrice != null ? retailPrice.hashCode() : 0);
        result = 31 * result + (itemName != null ? itemName.hashCode() : 0);
        result = 31 * result + (count != null ? count.hashCode() : 0);
        result = 31 * result + (summVat != null ? summVat.hashCode() : 0);
        result = 31 * result + (summInclVat != null ? summInclVat.hashCode() : 0);
        result = 31 * result + (expireDate != null ? expireDate.hashCode() : 0);
        return result;
    }

    public static InvoicesLinesEntity createInvoiceLineEntityFromInvoiceLine(InvoiceLine line){
        String defaultExpireDate = "9999-99-99";

        if(!line.getExpireDate().equals(""))
            defaultExpireDate = line.getExpireDate();

        return new InvoicesLinesEntity(
                line.getId(),
                line.getLineNumber(),
                line.getInvoiceNumber(),
                line.getItemId(),
                line.getVendorPrice(),
                (byte)line.getVat(),
                (byte)line.getExtraPrice(),
                line.getRetailPrice(),
                line.getItemName(),
                line.getCount(),
                line.getSummVat(),
                line.getSummIncludeVat(),
                defaultExpireDate
        );
    }
}
