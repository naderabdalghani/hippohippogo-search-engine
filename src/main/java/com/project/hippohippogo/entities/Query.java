package com.project.hippohippogo.entities;


import com.project.hippohippogo.ids.QueryId;
import javax.persistence.*;

@Entity
@IdClass(QueryId.class)
@Table(name = "queries")
public class Query {
    @Id
    @Column(name= "\"user_ip\"")
    private String userIp;
    @Id
    @Column(name="\"query\"")
    private String query;
    @Column(name="\"hits\"")
    private int hits;

    public Query() {

    }

    public Query(String userIp, String query) {
        this.userIp = userIp;
        this.query = query;
        hits = 1;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String ip) {
        this.userIp = ip;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getHits() {
        return hits;
    }

    public void incrementHits() {
        hits++;
    }
}
