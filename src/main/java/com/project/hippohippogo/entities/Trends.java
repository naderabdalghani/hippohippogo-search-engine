package com.project.hippohippogo.entities;

import com.project.hippohippogo.ids.TrendsId;
import com.project.hippohippogo.ids.WordId;

import javax.persistence.*;



@Entity
@IdClass(TrendsId.class)
@Table(name = "trends")
public class Trends {
    @Id
    @Column(name="\"person\"")
    private String person;
    @Id
    @Column(name="\"region\"")
    private String region;
    @Column(name="\"hits\"")
    private int hits;

    public Trends() {
    }

    public Trends(String person,String region) {
        this.person = person;
        this.hits=1;
        this.region=region;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
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
