package com.project.hippohippogo.repositories;
import com.project.hippohippogo.entities.PagesConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagesConnectionRepository extends JpaRepository<PagesConnection, Integer> {

}