package com.project.hippohippogo.entities;

import javax.persistence.*;

@Entity
@Table(name = "images")
public class image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String description;
    private String link;

    public image() { }

    public image(String description, String link) {
        SetDescription(description);
        SetLink(link);
    }

    public void SetDescription(String description) {
        this.description = description;
    }

    public void SetLink(String link) {
        this.link = link;
    }
}
