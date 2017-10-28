package entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "sales_header", schema = "minimag", catalog = "")
public class SalesHeaderEntity {
    private int id;
    private String salesNumber;
    private Double summ;
    private String salesType;
    private String payment;
    private Timestamp lastcreateupdate;

    @Id
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
    @Column(name = "summ", nullable = true, precision = 0)
    public Double getSumm() {
        return summ;
    }

    public void setSumm(Double summ) {
        this.summ = summ;
    }

    @Basic
    @Column(name = "sales_type", nullable = true, length = 45)
    public String getSalesType() {
        return salesType;
    }

    public void setSalesType(String salesType) {
        this.salesType = salesType;
    }

    @Basic
    @Column(name = "payment", nullable = true, length = 45)
    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    @Basic
    @Column(name = "lastcreateupdate", nullable = false)
    public Timestamp getLastcreateupdate() {
        return lastcreateupdate;
    }

    public void setLastcreateupdate(Timestamp lastcreateupdate) {
        this.lastcreateupdate = lastcreateupdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SalesHeaderEntity that = (SalesHeaderEntity) o;

        if (id != that.id) return false;
        if (salesNumber != null ? !salesNumber.equals(that.salesNumber) : that.salesNumber != null) return false;
        if (summ != null ? !summ.equals(that.summ) : that.summ != null) return false;
        if (salesType != null ? !salesType.equals(that.salesType) : that.salesType != null) return false;
        if (payment != null ? !payment.equals(that.payment) : that.payment != null) return false;
        if (lastcreateupdate != null ? !lastcreateupdate.equals(that.lastcreateupdate) : that.lastcreateupdate != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (salesNumber != null ? salesNumber.hashCode() : 0);
        result = 31 * result + (summ != null ? summ.hashCode() : 0);
        result = 31 * result + (salesType != null ? salesType.hashCode() : 0);
        result = 31 * result + (payment != null ? payment.hashCode() : 0);
        result = 31 * result + (lastcreateupdate != null ? lastcreateupdate.hashCode() : 0);
        return result;
    }
}
