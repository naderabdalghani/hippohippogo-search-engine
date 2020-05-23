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
    private double rank;
    @Column(name= "\"out_links\"")
    private int out_links;

    public  PageRank(){}
    public PageRank(String page,float rank,int out_links){
        this.setPage(page);
        this.setRank(rank);
        this.setOut_links(out_links);
    }

    public void setPage(String page) {
        this.page = page;
    }

    public void setRank(double rank) {
        this.rank = rank;
    }

    public double getRank() {
        return rank;
    }

    public String getPage() {
        return page;
    }

    public void setOut_links(int num_referring_pages) {
        this.out_links = num_referring_pages;
    }

    public int getOut_links() {
        return out_links;
    }
}
