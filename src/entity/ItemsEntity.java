package entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "items", schema = "minimag")
public class ItemsEntity {
    private int id;
    private String vendorCode;
    private String name;
    private Timestamp createupdate;
    private String vendorCountry;

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
    @Column(name = "vendor_code", nullable = false, length = 45)
    public String getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 45)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "createupdate", nullable = false)
    public Timestamp getCreateupdate() {
        return createupdate;
    }

    public void setCreateupdate(Timestamp createupdate) {
        this.createupdate = createupdate;
    }

    @Basic
    @Column(name = "vendor_country", nullable = true, length = 45)
    public String getVendorCountry() {
        return vendorCountry;
    }

    public void setVendorCountry(String vendorCountry) {
        this.vendorCountry = vendorCountry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemsEntity that = (ItemsEntity) o;

        if (id != that.id) return false;
        if (vendorCode != null ? !vendorCode.equals(that.vendorCode) : that.vendorCode != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (createupdate != null ? !createupdate.equals(that.createupdate) : that.createupdate != null) return false;
        if (vendorCountry != null ? !vendorCountry.equals(that.vendorCountry) : that.vendorCountry != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (vendorCode != null ? vendorCode.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (createupdate != null ? createupdate.hashCode() : 0);
        result = 31 * result + (vendorCountry != null ? vendorCountry.hashCode() : 0);
        return result;
    }
}
