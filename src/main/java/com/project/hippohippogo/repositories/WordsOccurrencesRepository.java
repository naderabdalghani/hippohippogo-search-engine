package com.project.hippohippogo.repositories;

import com.project.hippohippogo.entities.Words;
import com.project.hippohippogo.entities.WordsOccurrences;
import com.project.hippohippogo.ids.WordId;
import com.project.hippohippogo.ids.WordOccId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordsOccurrencesRepository extends JpaRepository<WordsOccurrences, WordOccId> {

}