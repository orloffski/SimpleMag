package entity;

import javax.persistence.*;

@Entity
@Table(name = "counterparties", schema = "minimag")
public class CounterpartiesEntity {
    private int id;
    private String name;
    private String adress;
    private String unn;

    public CounterpartiesEntity() {
    }

    public CounterpartiesEntity(int id, String name, String adress, String unn) {
        this.id = id;
        this.name = name;
        this.adress = adress;
        this.unn = unn;
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
    @Column(name = "name", nullable = false, length = 150)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "adress", nullable = true, length = 250)
    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    @Basic
    @Column(name = "UNN", nullable = true, length = 45)
    public String getUnn() {
        return unn;
    }

    public void setUnn(String unn) {
        this.unn = unn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CounterpartiesEntity that = (CounterpartiesEntity) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (adress != null ? !adress.equals(that.adress) : that.adress != null) return false;
        if (unn != null ? !unn.equals(that.unn) : that.unn != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (adress != null ? adress.hashCode() : 0);
        result = 31 * result + (unn != null ? unn.hashCode() : 0);
        return result;
    }
}
