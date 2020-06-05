package com.project.hippohippogo.repositories;

import com.project.hippohippogo.entities.Image;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
    public List<Image> findAllByIdIn(List<Integer> id, Pageable pageable);

    @Query(value = "UPDATE images SET indexed=TRUE  WHERE id =?1", nativeQuery = true)
    @Modifying
    @Transactional
    void setImagesIndexed(int id);

    @Query(value = "SELECT title FROM images WHERE indexed=FALSE", nativeQuery = true)
    ArrayList<String> getImageContent();
    @Query(value = "SELECT id FROM images WHERE indexed=FALSE", nativeQuery = true)
    ArrayList<Integer> getImagesIds();

    // Return length of description
    @Query(value = "SELECT length FROM images WHERE id = ?1", nativeQuery = true)
    int getImageDescriptionLength(int id);

    // Return page link
    @Query(value = "SELECT source_link FROM images WHERE id = ?1", nativeQuery = true)
    String getImageLink(int id);

    // Return image region
    @Query(value = "SELECT region FROM images WHERE id = ?1", nativeQuery = true)
    String getImageRegion(int id);

    // Return image date
    @Query(value = "SELECT date_published FROM images WHERE id = ?1", nativeQuery = true)
    Date getImageDate(int id);

    List<Image> findImageByImageLinkAndSourceLink(String imageLink, String SourceLink);
}
