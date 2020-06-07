package com.project.hippohippogo.entities;

import javax.persistence.*;

@Entity
@Table(name = "pages")
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "pages_gen")
    @Column(name="\"id\"")
    private Integer id;
    @Column(name="\"content\"")
    private String content;
    @Column(name="\"link\"")
    private String link;
    @Column(name="\"length\"")
    private int length;
    @Column(name="\"title\"")
    private String title;
    @Column(name="\"description\"")
    private String description;
    @Column(name="\"region\"")
    private String region;
    @Column(name="\"date_published\"")
    private String date_published;
    @Column(name="\"indexed\"")
    private Boolean indexed;

    public Page() {
    }

    public Page(String content, String link, int length, String title, String description, String region, String date_published, Boolean indexed) {
        setContent(content);
        setLink(link);
        setLength(length);
        setTitle(title);
        setDescription(description);
        setRegion(region);
        setDate_Published(date_published);
        setIndexed(indexed);
    }

    public Page(String content, String link, int length, String title, String description, String region, String date_published) {
        setContent(content);
        setLink(link);
        setLength(length);
        setTitle(title);
        setDescription(description);
        setRegion(region);
        setDate_Published(date_published);
    }

    public Page(String content, String link, int length, String title, String description, String region, Boolean indexed) {
        setContent(content);
        setLink(link);
        setLength(length);
        setTitle(title);
        setDescription(description);
        setRegion(region);
        setIndexed(indexed);
    }

    public Page(String content, String link, int length, String title, String description, String region) {
        setContent(content);
        setLink(link);
        setLength(length);
        setTitle(title);
        setDescription(description);
        setRegion(region);
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setDate_Published(String date_published) {
        this.date_published = date_published;
    }

    public void setIndexed(Boolean indexed) {
        this.indexed = indexed;
    }

    public Integer getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getLink() {
        return link;
    }

    public int getLength() {
        return length;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getRegion() {
        return region;
    }

    public String getDate_published() {
        return date_published;
    }

    public Boolean getIndexed() {
        return indexed;
    }
}
