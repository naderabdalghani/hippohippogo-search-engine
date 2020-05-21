package com.project.hippohippogo.entities;
import javax.persistence.*;

@Entity
@Table(name = "words")
public class Words {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String word;
    private int docid;
    private int indexofword;


    public void setId(Long id) {
        this.id = id;
    }

    public Words() {
    }
    public Words(Long id,String word,int docid,int indexofword) {
        this.word = word;
        this.docid = docid;
        this.indexofword = indexofword;
    }

    public void setDocid(int docid) {
        this.docid = docid;
    }

    public void setIndexofword(int indexofword) {
        this.indexofword = indexofword;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Long getId() {
        return id;
    }

    public int getDocid() {
        return docid;
    }

    public int getIndexofword() {
        return indexofword;
    }

    public String getWord() {
        return word;
    }
}
