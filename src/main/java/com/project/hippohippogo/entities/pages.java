package com.project.hippohippogo.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pages")
public class pages {

    @Id
    private String link;
    private String content;

    public pages() {

    }

    public pages(String link, String content) {
        this.link = link;
        this.content = content;
    }

    public void set_link(String link) {
        this.link = link;
    }

    public void set_content(String content) {
        this.content = content;
    }
}
