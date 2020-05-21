package com.project.hippohippogo.repositories;
import com.project.hippohippogo.entities.Words;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordsRepository extends JpaRepository<Words,Long> {

}