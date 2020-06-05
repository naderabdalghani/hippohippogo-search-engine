package com.project.hippohippogo.entities;

import com.project.hippohippogo.ids.WordId;

import javax.persistence.*;

@Entity
@IdClass(WordId.class)
@Table(name = "words")
public class Words {


    @Id
    @Column(name="\"word\"")
    private String word;
    @Id
    @Column(name="\"doc_id\"")
    private int doc_id;
    @Id
    @Column(name="\"index_of_word\"")
    private int index_of_word;



    public Words() {
    }
    public Words( String word, int doc_id, int index_of_word) {
        this.word = word;
        this.doc_id = doc_id;
        this.index_of_word = index_of_word;
    }

    public void setDoc_id(int docid) {
        this.doc_id = docid;
    }

    public void setIndex_of_word(int indexofword) {
        this.index_of_word = indexofword;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getDoc_id() {
        return doc_id;
    }

    public int getIndex_of_word() {
        return index_of_word;
    }

    public String getWord() {
        return word;
    }
}
