package com.project.hippohippogo.entities;

import javax.persistence.*;

@Entity
@Table(name = "pages")
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String link;
    private String title;
    private String content;
    private String description;
    private int length;

    public Page() {
    }

    public Page(String link, String title, String content, String description) {
        set_link(link);
        set_title(title);
        set_content(content);
        set_description(description);
    }

    public void set_link(String link) {
        this.link = link;
    }

    public void set_title(String title) {
        this.title = title;
    }

    public void set_content(String content) {
        this.content = content;
    }

    public void set_length(int length) {
        this.length = length;
    }

    public void set_description(String description) {
        this.description = description;
    }

    public String get_description() {
        return description;
    }

    public String get_title() {
        return title;
    }

    public int getLength() {
        return length;
    }

    public String getLink() {
        return link;
    }
}
