package entity;

import org.hibernate.annotations.Entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "units")
public class Units implements Serializable{

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "unit")
    private String unit;

    public Units() {
    }

    public Units(String unit) {
        this.unit = unit;
    }

    public long getId() {
    return id;
  }

    public void setId(long id) {
    this.id = id;
  }


    public String getUnit() {
    return unit;
  }

    public void setUnit(String unit) {
    this.unit = unit;
  }

}
