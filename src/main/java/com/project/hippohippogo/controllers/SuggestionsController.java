package com.project.hippohippogo.controllers;

import com.project.hippohippogo.entities.SearchQuery;
import com.project.hippohippogo.repositories.QueriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class SuggestionsController {

    private QueriesRepository queriesRepository;

    @Autowired
    public void setQueriesRepository(QueriesRepository queriesRepository) {
        this.queriesRepository = queriesRepository;
    }

    @GetMapping("/suggestions")
    public String getSuggestions(@RequestParam("query") String query) throws JSONException {
        List<SearchQuery> searchQueries = queriesRepository.findAll(Sort.by(Sort.Direction.DESC, "hits"));
        ArrayList<String> queriesStrings = new ArrayList<>();
        String re = "^" + query.toLowerCase();
        Pattern pattern = Pattern.compile(re, Pattern.CASE_INSENSITIVE);

        int suggestionsLimit = 8;

        for (SearchQuery searchQuery : searchQueries) {
            Matcher matcher = pattern.matcher(searchQuery.getQuery());
            if (matcher.find()) {
                queriesStrings.add(searchQuery.getQuery());
                if (queriesStrings.size() > suggestionsLimit) {
                    break;
                }
            }
        }
        return new JSONObject().put("query", "Unit").put("suggestions", JSONObject.wrap(queriesStrings)).toString();
    }
}