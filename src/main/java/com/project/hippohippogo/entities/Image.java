package com.project.hippohippogo.entities;

import javax.persistence.*;

@Entity
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "image_gen")
    @Column(name="\"id\"")
    private Integer id;
    @Column(name="\"image_link\"")
    private String imageLink;
    @Column(name="\"source_link\"")
    private String sourceLink;
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


    public Image() { }

    public Image(String imageLink, String sourceLink, String title, int length, String region, String date_published, Boolean indexed) {
        setImageLink(imageLink);
        setSourceLink(sourceLink);
        setTitle(title);
        setLength(length);
        setRegion(region);
        setDate_published(date_published);
        setIndex(indexed);
    }

    public Image(String imageLink, String sourceLink, String title, int length, String region, Boolean indexed) {
        setImageLink(imageLink);
        setSourceLink(sourceLink);
        setTitle(title);
        setLength(length);
        setRegion(region);
        setIndex(indexed);
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setImageLink(String image_link) {
        this.imageLink = image_link;
    }

    public void setSourceLink(String source_link) {
        this.sourceLink = source_link;
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

    public String getImageLink() {
        return imageLink;
    }

    public String getSourceLink() {
        return sourceLink;
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
