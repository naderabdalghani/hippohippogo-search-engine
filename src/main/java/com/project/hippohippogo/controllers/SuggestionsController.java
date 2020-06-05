package com.project.hippohippogo.controllers;

import com.project.hippohippogo.repositories.QueriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SuggestionsController {

    private QueriesRepository queriesRepository;

    @Autowired
    public void setQueriesRepository(QueriesRepository queriesRepository) {
        this.queriesRepository = queriesRepository;
    }

    @GetMapping("/suggestions")
    public String getSuggestions(@RequestParam("query") String query, HttpServletRequest request) throws JSONException {
        String userIp = request.getRemoteAddr();
        int personalSuggestionsLimit = 3;
        int globalSuggestionsLimit = 5;
        String pattern = "^" + query.toLowerCase();

        List<String> suggestionsStrings = new ArrayList<>();
        suggestionsStrings.addAll(queriesRepository.findPersonalSuggestions(userIp, pattern, personalSuggestionsLimit));
        suggestionsStrings.addAll(queriesRepository.findGlobalSuggestions(pattern, globalSuggestionsLimit));
        suggestionsStrings = suggestionsStrings.stream().distinct().collect(Collectors.toList());
        return new JSONObject().put("query", "Unit").put("suggestions", JSONObject.wrap(suggestionsStrings)).toString();
    }
}