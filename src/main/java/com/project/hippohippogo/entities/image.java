package com.project.hippohippogo.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "images")
public class image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String image_link;
    private String source_link;
    private String title;
    private int length;
    private Date date_published;

    public image() {
    }

    public image(Integer id, String image_link, String source_link, String title, int length, Date date_published) {
        this.id = id;
        this.image_link = image_link;
        this.source_link = source_link;
        this.title = title;
        this.length = length;
        this.date_published = date_published;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    public void setSource_link(String source_link) {
        this.source_link = source_link;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setDate_published(Date date_published) {
        this.date_published = date_published;
    }

    public Integer getId() {
        return id;
    }

    public String getImage_link() {
        return image_link;
    }

    public String getSource_link() {
        return source_link;
    }

    public String getTitle() {
        return title;
    }

    public int getLength() {
        return length;
    }

    public Date getDate_published() {
        return date_published;
    }
}
