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
    private int lenght;

    public Page() {
    }

    public Page(String link, String title, String content) {
        set_link(link);
        set_title(title);
        set_content(content);
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
        this.lenght = length;
    }
}
