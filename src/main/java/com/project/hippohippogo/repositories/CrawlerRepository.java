package com.project.hippohippogo.repositories;

import com.project.hippohippogo.entities.pages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrawlerRepository extends JpaRepository<pages, String> {
}
