package entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "invoices_headers", schema = "minimag")
public class InvoicesHeadersEntity {
    private int id;
    private String number;
    private String type;
    private String status;
    private String counterparty;
    private Double count;
    private Double summ;
    private Integer counterpartyId;
    private Timestamp lastcreated;
    private Integer recipientId;
    private String recipientName;
    private Double fullSumm;
    private String ttnNumber;
    private String ttnDate;

    public InvoicesHeadersEntity() {
    }

    public InvoicesHeadersEntity(int id, String number, String type, String status, String counterparty, Double count, Double summ, Integer counterpartyId, Timestamp lastcreated, Integer recipientId, String recipientName, Double fullSumm, String ttnNumber, String ttnDate) {
        this.id = id;
        this.number = number;
        this.type = type;
        this.status = status;
        this.counterparty = counterparty;
        this.count = count;
        this.summ = summ;
        this.counterpartyId = counterpartyId;
        this.lastcreated = lastcreated;
        this.recipientId = recipientId;
        this.recipientName = recipientName;
        this.fullSumm = fullSumm;
        this.ttnNumber = ttnNumber;
        this.ttnDate = ttnDate;
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
    @Column(name = "number", nullable = true, length = 45)
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Basic
    @Column(name = "type", nullable = true, length = 45)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "status", nullable = true, length = 45)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "counterparty", nullable = true, length = 45)
    public String getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(String counterparty) {
        this.counterparty = counterparty;
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
    @Column(name = "summ", nullable = true, precision = 0)
    public Double getSumm() {
        return summ;
    }

    public void setSumm(Double summ) {
        this.summ = summ;
    }

    @Basic
    @Column(name = "counterparty_id", nullable = true)
    public Integer getCounterpartyId() {
        return counterpartyId;
    }

    public void setCounterpartyId(Integer counterpartyId) {
        this.counterpartyId = counterpartyId;
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
    @Column(name = "recipient_id", nullable = true)
    public Integer getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Integer recipientId) {
        this.recipientId = recipientId;
    }

    @Basic
    @Column(name = "recipient_name", nullable = true, length = 45)
    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    @Basic
    @Column(name = "full_summ", nullable = true, precision = 0)
    public Double getFullSumm() {
        return fullSumm;
    }

    public void setFullSumm(Double fullSumm) {
        this.fullSumm = fullSumm;
    }

    @Basic
    @Column(name = "ttn_number", nullable = true, length = 45)
    public String getTtnNumber() {
        return ttnNumber;
    }

    public void setTtnNumber(String ttnNumber) {
        this.ttnNumber = ttnNumber;
    }

    @Basic
    @Column(name = "ttn_date", nullable = true, length = 45)
    public String getTtnDate() {
        return ttnDate;
    }

    public void setTtnDate(String ttnDate) {
        this.ttnDate = ttnDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InvoicesHeadersEntity that = (InvoicesHeadersEntity) o;

        if (id != that.id) return false;
        if (number != null ? !number.equals(that.number) : that.number != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (counterparty != null ? !counterparty.equals(that.counterparty) : that.counterparty != null) return false;
        if (count != null ? !count.equals(that.count) : that.count != null) return false;
        if (summ != null ? !summ.equals(that.summ) : that.summ != null) return false;
        if (counterpartyId != null ? !counterpartyId.equals(that.counterpartyId) : that.counterpartyId != null)
            return false;
        if (lastcreated != null ? !lastcreated.equals(that.lastcreated) : that.lastcreated != null) return false;
        if (recipientId != null ? !recipientId.equals(that.recipientId) : that.recipientId != null) return false;
        if (recipientName != null ? !recipientName.equals(that.recipientName) : that.recipientName != null)
            return false;
        if (fullSumm != null ? !fullSumm.equals(that.fullSumm) : that.fullSumm != null) return false;
        if (ttnNumber != null ? !ttnNumber.equals(that.ttnNumber) : that.ttnNumber != null) return false;
        if (ttnDate != null ? !ttnDate.equals(that.ttnDate) : that.ttnDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (number != null ? number.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (counterparty != null ? counterparty.hashCode() : 0);
        result = 31 * result + (count != null ? count.hashCode() : 0);
        result = 31 * result + (summ != null ? summ.hashCode() : 0);
        result = 31 * result + (counterpartyId != null ? counterpartyId.hashCode() : 0);
        result = 31 * result + (lastcreated != null ? lastcreated.hashCode() : 0);
        result = 31 * result + (recipientId != null ? recipientId.hashCode() : 0);
        result = 31 * result + (recipientName != null ? recipientName.hashCode() : 0);
        result = 31 * result + (fullSumm != null ? fullSumm.hashCode() : 0);
        result = 31 * result + (ttnNumber != null ? ttnNumber.hashCode() : 0);
        result = 31 * result + (ttnDate != null ? ttnDate.hashCode() : 0);
        return result;
    }
}
