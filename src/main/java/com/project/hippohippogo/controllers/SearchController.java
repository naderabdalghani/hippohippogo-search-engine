package com.project.hippohippogo.controllers;

import com.project.hippohippogo.entities.DummyItem;
import com.project.hippohippogo.entities.Page;
import com.project.hippohippogo.entities.Query;
import com.project.hippohippogo.ids.QueryId;
import com.project.hippohippogo.repositories.DummyRepository;
import com.project.hippohippogo.repositories.PagesRepository;
import com.project.hippohippogo.repositories.QueriesRepository;
import com.project.hippohippogo.services.QueryProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class SearchController {

    private DummyRepository dummyRepository;
    private QueriesRepository queriesRepository;
    private PagesRepository pagesRepository;
    private QueryProcessorService queryProcessorService;

    @Autowired
    public void setDummyRepository(DummyRepository dummyRepository) {
        this.dummyRepository = dummyRepository;
    }

    @Autowired
    public void setQueriesRepository(QueriesRepository queriesRepository) {
        this.queriesRepository = queriesRepository;
    }

    @Autowired
    public void setPagesRepository(PagesRepository pagesRepository) {
        this.pagesRepository = pagesRepository;
    }

    @Autowired
    public void setQueryProcessorService(QueryProcessorService queryProcessorService) {
        this.queryProcessorService = queryProcessorService;
    }

    @RequestMapping(value = "/search", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public List<Page> getWebResultsAsJSON(@RequestParam("q") String queryString, @RequestParam(value = "offset", required = false, defaultValue = "0") int offset, @RequestParam(value = "limit", required = false, defaultValue = "20") int limit, @RequestParam(value = "region", required = false, defaultValue = "") String region) {
        // List<Integer> resultsIds = queryProcessorService.getPageResults(queryString);
        List<Integer> resultsIds = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
        Pageable pageable = PageRequest.of(offset, limit);
        List<Page> results = pagesRepository.findAllByIdIn(resultsIds, pageable);
        System.out.print(results);
        return results;
    }

    @RequestMapping(value = "/search", produces = "text/html", method = RequestMethod.GET)
    public String getWebResultsAsHTML(Model model, @RequestParam("q") String queryString, @RequestParam(value = "offset", required = false, defaultValue = "0") int offset, @RequestParam(value = "limit", required = false, defaultValue = "20") int limit, @RequestParam(value = "region", required = false, defaultValue = "") String region, HttpServletRequest request) {
        // Return to landing page if query is empty
        if (queryString.equals("")) {
            return "index";
        }

        // Add query for suggestions if new or increment its hits if it already exists
        region = region.length() == 0 ? null : region;
        String userIp = request.getRemoteAddr();
        QueryId queryId = new QueryId(userIp, queryString.toLowerCase());
        Optional<Query> query = queriesRepository.findById(queryId);
        if (query.isPresent()) {
            query.get().incrementHits();
            queriesRepository.save(query.get());
        } else {
            Query newQuery = new Query(userIp, queryString.toLowerCase());
            queriesRepository.save(newQuery);
        }

        // Fetch Results
        // List<Integer> resultsIds = queryProcessorService.getPageResults(queryString);
        List<Integer> resultsIds = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
        Pageable pageable = PageRequest.of(offset, limit);
        List<Page> results = pagesRepository.findAllByIdIn(resultsIds, pageable);
        model.addAttribute("query", queryString);
        model.addAttribute("results", results);
        model.addAttribute("region", region);
        return "results";
    }

    @GetMapping("/img")
    public String getImgResults(Model model) {
        List<DummyItem> items = (List<DummyItem>) dummyRepository.findAll();
        model.addAttribute("items", items);
        return "showDummyData";
    }
}