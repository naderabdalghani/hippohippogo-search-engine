package com.project.hippohippogo.repositories;

import com.project.hippohippogo.entities.Page;
import com.project.hippohippogo.entities.image;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;



@Repository
public interface ImagesRepository extends JpaRepository<image, Integer> {
    public List<Page> findAllByIdIn(List<Integer> id, Pageable pageable);

    @Query(value = "UPDATE images SET indexed=TRUE  WHERE id =?1", nativeQuery = true)
    @Modifying
    @Transactional
    void setImagesIndexed(int id);

    @Query(value = "SELECT title FROM images WHERE indexed=FALSE", nativeQuery = true)
    ArrayList<String> getImageContent();
    @Query(value = "SELECT id FROM images WHERE indexed=FALSE", nativeQuery = true)
    ArrayList<Integer> getImagesIds();
}
