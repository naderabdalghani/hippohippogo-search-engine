package com.project.hippohippogo.repositories;

import com.project.hippohippogo.entities.WordsOccurrences;
import com.project.hippohippogo.ids.WordOccId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WordsOccurrencesRepository extends JpaRepository<WordsOccurrences, WordOccId> {

    // Return title count of word from doc
    @Query(value = "SELECT titlecount FROM words_occurrences WHERE doc_id = ?1 and word = ?2", nativeQuery = true)
    int getTitleCount(int doc_id,String word);

    // Return list with word and distinct documents containing it
    @Query(value = "SELECT headercount FROM words_occurrences WHERE doc_id = ?1 and word = ?2", nativeQuery = true)
    int getHeaderCount(int doc_id,String word);

    // Check if word is exist or not
    @Query(value = "SELECT EXISTS(SELECT * from words_occurrences WHERE doc_id=?1 and word = ?2)", nativeQuery = true)
    int isWordExists(int doc_id,String word);

}