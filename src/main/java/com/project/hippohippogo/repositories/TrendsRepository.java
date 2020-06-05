package com.project.hippohippogo.repositories;


import com.project.hippohippogo.entities.Trends;
import com.project.hippohippogo.ids.TrendsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TrendsRepository extends JpaRepository<Trends, TrendsId> {
    @Query(value = "SELECT * FROM trends WHERE region = ?1 ORDER BY hits DESC LIMIT 10", nativeQuery = true)
    List<Trends> findTopTenByRegion(String region);

    @Query(value = "SELECT person, SUM(hits) as hits, region FROM trends GROUP BY person ORDER BY hits DESC LIMIT 10", nativeQuery = true)
    List<Trends> findTopTenOverall();

    @Query(value = "UPDATE trends SET hits=?3  WHERE person =?1 AND region=?2", nativeQuery = true)
    @Modifying
    @Transactional
    void updateHits(String person , String region,int hits);

}