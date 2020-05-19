package com.project.hippohippogo.entities;
import javax.persistence.*;

@Entity
@Table(name = "pages_connection")
public class PagesConnection {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String refered;
    private String refering;

    public PagesConnection() {
    }

    public void setRefered(String refered) {
        this.refered = refered;
    }

    public void setRefering(String refering) {
        this.refering = refering;
    }

    public String getRefered() {
        return refered;
    }

    public String getRefering() {
        return refering;
    }
}
