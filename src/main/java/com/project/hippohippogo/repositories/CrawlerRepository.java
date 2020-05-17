package com.project.hippohippogo.repositories;

import com.project.hippohippogo.entities.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrawlerRepository extends JpaRepository<Page, String> {
}
