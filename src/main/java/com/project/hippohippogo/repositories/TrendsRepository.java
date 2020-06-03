package com.project.hippohippogo.repositories;


import com.project.hippohippogo.entities.Trends;
import com.project.hippohippogo.ids.TrendsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrendsRepository extends JpaRepository<Trends, TrendsId> {

}