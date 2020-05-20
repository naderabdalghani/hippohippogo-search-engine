package com.project.hippohippogo.entities;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="\"page_rank\"")
public class PageRank {
    @Id
    @Column(name="\"page\"")
    private String page;
    @Column(name="\"rank\"")
    private float rank;

    public  PageRank(){}
    public PageRank(String page,float rank){
        this.setPage(page);
        this.setRank(rank);
    }

    public void setPage(String page) {
        this.page = page;
    }

    public void setRank(float rank) {
        this.rank = rank;
    }

    public float getRank() {
        return rank;
    }

    public String getPage() {
        return page;
    }
}
