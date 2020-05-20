package com.project.hippohippogo.entities;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "page_rank")
public class PageRank {
    @Id
    private String page;
    private float rank;

    public PageRank(){
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
