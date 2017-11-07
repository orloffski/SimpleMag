package entity;

import javax.persistence.*;

@Entity
@Table(name = "units", schema = "minimag")
public class UnitsEntity {
    private int id;
    private String unit;

    public UnitsEntity() {
    }

    public UnitsEntity(int id, String unit) {
        this.id = id;
        this.unit = unit;
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
    @Column(name = "unit", nullable = false, length = 45)
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnitsEntity that = (UnitsEntity) o;

        if (id != that.id) return false;
        if (unit != null ? !unit.equals(that.unit) : that.unit != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        return result;
    }

    public static UnitsEntity createUnitsEntity(int id, String unit){
        return new UnitsEntity(id, unit);
    }
}
