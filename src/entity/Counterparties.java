package entity;

import org.hibernate.annotations.Entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "counterparties")
public class Counterparties implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "adress")
    private String adress;

    @Column(name = "UNN")
    private String UNN;

    public Counterparties() {
    }

    public Counterparties(String name, String adress, String UNN) {
        this.name = name;
        this.adress = adress;
        this.UNN = UNN;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getUNN() {
        return UNN;
    }

    public void setUNN(String UNN) {
        this.UNN = UNN;
    }
}
