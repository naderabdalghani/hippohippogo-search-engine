package com.project.hippohippogo.repositories;

import com.project.hippohippogo.entities.Innerlink;
import com.project.hippohippogo.entities.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InnerlinkRepository extends JpaRepository<Innerlink, Integer> {
}
