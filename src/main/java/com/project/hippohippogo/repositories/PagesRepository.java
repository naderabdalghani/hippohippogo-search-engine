package com.project.hippohippogo.repositories;

import com.project.hippohippogo.entities.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface PagesRepository extends JpaRepository<Page, Integer> {
    public List<Page> findAllByIdIn(List<Integer> id, Pageable pageable);

    @Query(value = "UPDATE pages SET indexed=TRUE  WHERE id =?1", nativeQuery = true)
    @Modifying
    @Transactional
    void setWebPagesIndexed(int id);
    @Query(value = "SELECT content FROM pages WHERE indexed=FALSE ", nativeQuery = true)
    ArrayList<String> getWebPages();
    @Query(value = "SELECT id FROM pages WHERE indexed=FALSE", nativeQuery = true)
    ArrayList<Integer> getWebPagesIds();
}
