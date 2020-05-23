package com.project.hippohippogo.entities;


import javax.persistence.*;

@Entity
@Table(name = "search_queries")
public class SearchQuery {
    @Id
    private String query;
    private int hits;

    public SearchQuery() {

    }

    public SearchQuery(String query) {
        this.query = query;
        hits = 1;
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
