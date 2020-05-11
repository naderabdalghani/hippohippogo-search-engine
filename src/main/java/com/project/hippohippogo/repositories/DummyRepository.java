package com.project.hippohippogo.repositories;

import com.project.hippohippogo.entities.DummyItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DummyRepository extends JpaRepository<DummyItem, Long> {

}