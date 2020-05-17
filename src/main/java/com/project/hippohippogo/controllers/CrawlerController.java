package com.project.hippohippogo.controllers;

import com.project.hippohippogo.entities.pages;
import com.project.hippohippogo.repositories.CrawlerRepository;
import com.project.hippohippogo.repositories.DummyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CrawlerController {
    private CrawlerRepository repo;

    @Autowired
    public void setCrawlerRepository(CrawlerRepository repo) {
        this.repo = repo;
    }

    public pages insert() {
        pages P = new pages("fdafafa", "qewrqer");
        return repo.save(P);
    }

}
