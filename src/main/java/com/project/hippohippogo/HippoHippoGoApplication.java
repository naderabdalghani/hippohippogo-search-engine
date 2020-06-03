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
        CrawlerService crawlerService = applicationContext.getBean(CrawlerService.class);
        IndexerService indexer = applicationContext.getBean(IndexerService.class);
        RankerService rankerService = applicationContext.getBean(RankerService.class);
        //initialize(crawlerService,rankerService,indexer);
    }

    private static void initialize(CrawlerService crawlerService,RankerService rankerService, IndexerService indexer) {
        //crawlerService.Crawl();
        //indexer.main();
        rankerService.rankPages();
    }
}
