package com.project.hippohippogo.entities;


import com.project.hippohippogo.ids.QueryId;
import javax.persistence.*;

@Entity
@IdClass(QueryId.class)
@Table(name = "queries")
public class Query {
    @Id
    private String userIp;
    @Id
    private String query;
    @Id
    private String region;
    private int hits;

    public Query() {

    }

    public Query(String userIp, String query, String region) {
        this.userIp = userIp;
        this.query = query;
        this.region = region;
        hits = 1;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String ip) {
        this.userIp = ip;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
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
