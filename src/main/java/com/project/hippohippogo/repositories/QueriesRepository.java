package com.project.hippohippogo.repositories;

import com.project.hippohippogo.entities.Query;
import com.project.hippohippogo.ids.QueryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QueriesRepository extends JpaRepository<Query, QueryId> {
    @org.springframework.data.jpa.repository.Query(value = "SELECT query FROM queries WHERE ip = ?1 AND query REGEXP ?2 ORDER BY hits DESC LIMIT ?3", nativeQuery = true)
    List<String> findPersonalSuggestions(String userIp, String pattern, int limit);

    @org.springframework.data.jpa.repository.Query(value = "SELECT query FROM (SELECT query, SUM(hits) AS totalHits FROM queries WHERE query REGEXP ?1 GROUP BY query ORDER BY totalHits DESC LIMIT ?2) as Q", nativeQuery = true)
    List<String> findGlobalSuggestions(String pattern, int limit);
}