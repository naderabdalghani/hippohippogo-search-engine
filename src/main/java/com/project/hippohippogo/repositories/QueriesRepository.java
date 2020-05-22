package com.project.hippohippogo.repositories;

import com.project.hippohippogo.entities.SearchQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QueriesRepository extends JpaRepository<SearchQuery, String> {

}