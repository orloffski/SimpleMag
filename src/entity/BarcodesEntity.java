package entity;

import javax.persistence.*;

@Entity
@Table(name = "barcodes", schema = "minimag")
public class BarcodesEntity {
    private int id;
    private String barcode;
    private int itemId;

    public BarcodesEntity() {
    }

    public BarcodesEntity(int id, String barcode, int itemId) {
        this.id = id;
        this.barcode = barcode;
        this.itemId = itemId;
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
    @Column(name = "barcode", nullable = false, length = 45)
    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    @Basic
    @Column(name = "item_id", nullable = false)
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BarcodesEntity that = (BarcodesEntity) o;

        if (id != that.id) return false;
        if (itemId != that.itemId) return false;
        if (barcode != null ? !barcode.equals(that.barcode) : that.barcode != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (barcode != null ? barcode.hashCode() : 0);
        result = 31 * result + itemId;
        return result;
    }

    public static BarcodesEntity createBarcodesEntity(int id, String barcode, int itemId){
        return new BarcodesEntity(id, barcode, itemId);
    }
}
