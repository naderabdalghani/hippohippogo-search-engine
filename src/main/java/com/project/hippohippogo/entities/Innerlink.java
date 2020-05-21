package com.project.hippohippogo.entities;


import javax.persistence.*;

@Entity
@Table(name = "innerlinks")
public class Innerlink {

    @Id
    private String Base;
    private String Child;

    public Innerlink() {
    }

    public Innerlink(String Base, String Child) {
        set_Base(Base);
        set_Inner(Child);
    }

    public void set_Base(String Base) {
        this.Base = Base;
    }

    public void set_Inner(String Child) {
        this.Child = Child;
    }
}
