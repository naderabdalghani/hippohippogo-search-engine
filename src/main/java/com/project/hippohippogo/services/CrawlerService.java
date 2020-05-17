package com.project.hippohippogo.services;

import com.project.hippohippogo.entities.Page;
import com.project.hippohippogo.repositories.CrawlerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CrawlerService {
    private CrawlerRepository repo;

    @Autowired
    public void setCrawlerRepository(CrawlerRepository repo) {
        this.repo = repo;
    }

    public Page insert() {
        Page P = new Page("fdafafa", "qewrqer");
        return repo.save(P);
    }

}
