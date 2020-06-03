package com.project.hippohippogo.entities;
import javax.persistence.*;

@Entity
@Table(name = "images_words")
public class images_words {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="\"id\"")
    private Long id;
    @Column(name="\"word\"")
    private String word;
    @Column(name="\"doc_id\"")
    private int doc_id;
    @Column(name="\"index_of_word\"")
    private int index_of_word;

    public images_words() {
    }

    public images_words( String word, int doc_id, int index_of_word) {
        this.id = id;
        this.word = word;
        this.doc_id = doc_id;
        this.index_of_word = index_of_word;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(int doc_id) {
        this.doc_id = doc_id;
    }

    public int getIndex_of_word() {
        return index_of_word;
    }

    public void setIndex_of_word(int index_of_word) {
        this.index_of_word = index_of_word;
    }


}
