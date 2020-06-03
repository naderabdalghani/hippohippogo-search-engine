package com.project.hippohippogo.entities;

import javax.persistence.*;

@Entity
@Table(name = "images")
public class image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="\"id\"")
    private Integer id;
    @Column(name="\"image_link\"")
    private String image_link;
    @Column(name="\"source_link\"")
    private String source_link;
    @Column(name="\"title\"")
    private String title;
    @Column(name="\"length\"")
    private Integer length;
    @Column(name="\"region\"")
    private String region;
    @Column(name="\"date_published\"")
    private String date_published;
    @Column(name="\"indexed\"")
    private Boolean indexed;


    public image() { }

    public image(String image_link, String source_link, String title, int length, String region, String date_published, Boolean indexed) {
        setImage_link(image_link);
        setSource_link(source_link);
        setTitle(title);
        setLength(length);
        setRegion(region);
        setDate_published(date_published);
        setIndex(indexed);
    }

    public image(String image_link, String source_link, String title, int length, String region, Boolean indexed) {
        setImage_link(image_link);
        setSource_link(source_link);
        setTitle(title);
        setLength(length);
        setRegion(region);
        setIndex(indexed);
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

    public void setRegion(String region) {
        this.region = region;
    }

    public void setDate_published(String date_published) {
        this.date_published = date_published;
    }

    public void setIndex(Boolean indexed) {
        this.indexed = indexed;
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

    public Integer getLength() {
        return length;
    }

    public String getRegion() {
        return region;
    }

    public String getDate_published() {
        return date_published;
    }

    public Boolean getIndex(Boolean indexed) {
        return indexed;
    }
}
