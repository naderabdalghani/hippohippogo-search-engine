package com.project.hippohippogo.ids;

import java.io.Serializable;
import java.util.Objects;

public class WordId implements Serializable {
    private String word;
    private int doc_id;
    private int index_of_word;

    public WordId() {
    }

    public WordId(String word, int doc_id, int index_of_word) {
        this.word = word;
        this.doc_id = doc_id;
        this.index_of_word = index_of_word;
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

    @Override
    public int hashCode() {
        return Objects.hash(word, doc_id, index_of_word);
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
        final WordId other = (WordId) obj;
        if (!Objects.equals(this.word, other.getWord())) {
            return false;
        }
        if (!Objects.equals(this.doc_id, other.getDoc_id())) {
            return false;
        }
        return Objects.equals(this.index_of_word, other.getIndex_of_word());
    }
}


