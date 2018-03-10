package entity;

import javax.persistence.*;

@Entity
@Table(name = "items_in_stock", schema = "minimag")
public class ItemsInStockEntity {
    private int id;
    private int idCounterparty;
    private String date;
    private String summ;

    public ItemsInStockEntity() {
    }

    public ItemsInStockEntity(int id, int idCounterparty, String date, String summ) {
        this.id = id;
        this.idCounterparty = idCounterparty;
        this.date = date;
        this.summ = summ;
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
    @Column(name = "id_counterparty", nullable = false, length = 11)
    public int getIdCounterparty() {
        return idCounterparty;
    }

    public void setIdCounterparty(int idCounterparty) {
        this.idCounterparty = idCounterparty;
    }

    @Basic
    @Column(name = "date", nullable = false)
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Basic
    @Column(name = "summ", nullable = false, length = 45)
    public String getSumm() {
        return summ;
    }

    public void setSumm(String summ) {
        this.summ = summ;
    }
}
