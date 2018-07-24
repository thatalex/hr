package by.spalex.diplom.hr.model;

import javax.persistence.*;

@Entity
@Table(name = "value")
public class Value {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "name")
    private String name = "";

    @Column(name = "rate")
    private double rate = 0.0;

    public Value(){}

    public Value(String name, double rate) {
        this.name = name;
        this.rate = rate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public double getRate() {
        return rate;
    }
}

