package com.project.hippohippogo.repositories;

import com.project.hippohippogo.entities.image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<image, Integer> {

}
