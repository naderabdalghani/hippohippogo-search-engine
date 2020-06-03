package com.project.hippohippogo.entities;

import javax.persistence.*;



@Entity
@Table(name = "trends")
public class Trends {
    @Id
    @Column(name="\"person\"")
    private String person;
    @Column(name="\"hits\"")
    private int hits;

    public Trends() {
    }

    public Trends(String person) {
        this.person = person;
        this.hits=1;
    }



    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }
    public void incrementHits() {
        hits++;
    }
}
