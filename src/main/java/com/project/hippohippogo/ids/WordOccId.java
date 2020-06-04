package com.project.hippohippogo.ids;

import java.io.Serializable;
import java.util.Objects;

public class WordOccId implements Serializable {
    private String word;
    private int doc_id;
    private int titlecount;
    private int headercount;

    public WordOccId() {
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

    @Override
    public int hashCode() {
        return Objects.hash(word, doc_id, titlecount,headercount);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WordOccId other = (WordOccId) obj;
        if (!Objects.equals(this.word, other.getWord())) {
            return false;
        }
        if (!Objects.equals(this.doc_id, other.getDoc_id())) {
            return false;
        }
        if (!Objects.equals(this.titlecount, other.getTitlecount())) {
            return false;
        }
        return Objects.equals(this.headercount, other.getHeadercount());
    }
}

