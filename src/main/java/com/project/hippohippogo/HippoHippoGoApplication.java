package com.project.hippohippogo;

import com.project.hippohippogo.services.CrawlerService;
import com.project.hippohippogo.services.RankerService;
import com.project.hippohippogo.services.IndexerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;

@SpringBootApplication
public class HippoHippoGoApplication {
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(HippoHippoGoApplication.class, args);
        //CrawlerService crawlerService = applicationContext.getBean(CrawlerService.class);
        //crawlerService.Crawl();
        IndexerService indexer = applicationContext.getBean(IndexerService.class);
        indexer.main();
        //RankerService rankerService = applicationContext.getBean(RankerService.class);
        //List<Integer> pagesIDs = rankerService.getURLs("football soccer");
    }

    private void initialize(CrawlerService crawlerService,RankerService rankerService) {
        crawlerService.Crawl();
        rankerService.rankPages();
    }
}
