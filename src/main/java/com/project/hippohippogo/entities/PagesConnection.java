package com.project.hippohippogo.entities;
import javax.persistence.*;

@Entity
@Table(name = "pages_connection")
public class PagesConnection {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "pages_connection_gen")
    private int id;
    private String referred;
    private String referring;

    public PagesConnection() {
    }

    public PagesConnection(String referring, String referred) {
        this.referred = referred;       //Child
        this.referring = referring;     //Base
    }

    public void setReferred(String refered) {
        this.referred = refered;
    }

    public void setReferring(String refering) {
        this.referring = refering;
    }

    public String getReferred() {
        return referred;
    }

    public String getReferring() {
        return referring;
    }
}
