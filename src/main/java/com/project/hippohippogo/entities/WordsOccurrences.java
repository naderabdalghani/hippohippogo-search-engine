package com.project.hippohippogo.entities;

import com.project.hippohippogo.ids.WordOccId;

import javax.persistence.*;


@Entity
@IdClass(WordOccId.class)
@Table(name = "words_occurrences")
public class WordsOccurrences {

    @Id
    @Column(name="\"word\"")
    private String word;
    @Id
    @Column(name="\"doc_id\"")
    private int doc_id;
    @Id
    @Column(name="\"titlecount\"")
    private int titlecount;
    @Id
    @Column(name="\"headercount\"")
    private int headercount;

    public WordsOccurrences() {
    }

    public WordsOccurrences(String word, int doc_id, int titlecount, int headercount) {
        this.word = word;
        this.doc_id = doc_id;
        this.titlecount = titlecount;
        this.headercount = headercount;
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

    public int getTitlecount() {
        return titlecount;
    }

    public void setTitlecount(int titlecount) {
        this.titlecount = titlecount;
    }

    public int getHeadercount() {
        return headercount;
    }

    public void setHeadercount(int headercount) {
        this.headercount = headercount;
    }
}

