package com.project.hippohippogo.controllers;

import com.project.hippohippogo.entities.DummyItem;
import com.project.hippohippogo.entities.SearchQuery;
import com.project.hippohippogo.repositories.DummyRepository;
import com.project.hippohippogo.repositories.QueriesRepository;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class SearchController {

    private DummyRepository dummyRepository;
    private QueriesRepository queriesRepository;

    @Autowired
    public void setDummyRepository(DummyRepository dummyRepository) {
        this.dummyRepository = dummyRepository;
    }

    @Autowired
    public void setQueriesRepository(QueriesRepository queriesRepository) {
        this.queriesRepository = queriesRepository;
    }

    @GetMapping("/search")
    public String getWebResults(Model model, @RequestParam("q") String queryString) {
        // Return to landing page if query is empty
        if (queryString.equals("")) {
            return "index";
        }

        // Register query for suggestions
        Optional<SearchQuery> searchQuery = queriesRepository.findById(queryString);
        if (!searchQuery.isPresent()) {
            SearchQuery newSearchQuery = new SearchQuery(queryString.toLowerCase());
            queriesRepository.save(newSearchQuery);
        }
        else {
            searchQuery.get().setQuery(searchQuery.get().getQuery().toLowerCase());
            searchQuery.get().incrementHits();
            queriesRepository.save(searchQuery.get());
        }

        // Get Results
        var items = (List<DummyItem>) dummyRepository.findAll();
        model.addAttribute("items", items);
        return "showDummyData";
    }

    @GetMapping("/img")
    public String getImgResults(Model model) {
        var items = (List<DummyItem>) dummyRepository.findAll();
        model.addAttribute("items", items);
        return "showDummyData";
    }
}