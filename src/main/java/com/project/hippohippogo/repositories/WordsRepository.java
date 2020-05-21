package com.project.hippohippogo.repositories;
import com.project.hippohippogo.entities.Words;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordsRepository extends JpaRepository<Words,Long> {

    // Return list with word and distinct documents containing it
    @Query(value = "SELECT DISTINCT docid FROM words WHERE word = ?1", nativeQuery = true)
    List<Integer> getDocIdContainingWord(String word);

    // Return word count in specific document
    @Query(value = "SELECT COUNT(*) FROM words WHERE word = ?1 and docid = ?2", nativeQuery = true)
    int getWordCountInDoc(String word,int doc);
}