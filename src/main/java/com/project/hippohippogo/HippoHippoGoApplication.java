package com.project.hippohippogo;

import com.project.hippohippogo.controllers.SearchController;
import com.project.hippohippogo.services.CrawlerService;
import com.project.hippohippogo.services.RankerService;
import com.project.hippohippogo.services.IndexerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class HippoHippoGoApplication {
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(HippoHippoGoApplication.class, args);
//        CrawlerService crawlerService = applicationContext.getBean(CrawlerService.class);
//        crawlerService.Crawl();
        System.out.print("the indexer begins######################################################################################################################3");
        IndexerService indexer = applicationContext.getBean(IndexerService.class);
        //indexer.main();

        //RankerService rankerService = applicationContext.getBean(RankerService.class);
        //List<Integer> pagesIDs = rankerService.getURLs("football soccer");
    }

    private static void initialize(CrawlerService crawlerService,RankerService rankerService, IndexerService indexer) {
        //crawlerService.Crawl();
        //indexer.main();
        rankerService.rankPages();
    }
}
