package com.project.hippohippogo.repositories;


import com.project.hippohippogo.entities.Trends;
import com.project.hippohippogo.ids.TrendsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TrendsRepository extends JpaRepository<Trends, TrendsId> {

    @Query(value = "UPDATE trends SET hits=?3  WHERE person =?1 AND region=?2", nativeQuery = true)
    @Modifying
    @Transactional
    void updateHits(String person , String region,int hits);

}