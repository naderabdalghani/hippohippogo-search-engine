package com.project.hippohippogo.repositories;

import com.project.hippohippogo.entities.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface PagesRepository extends JpaRepository<Page, Integer> {

    @Query(value = "SELECT content FROM pages", nativeQuery = true)
    ArrayList<String> getWebPages();
    @Query(value = "SELECT id FROM pages", nativeQuery = true)
    ArrayList<Integer> getWebPagesIds();
}
