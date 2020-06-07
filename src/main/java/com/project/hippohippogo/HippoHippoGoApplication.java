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

        //////////////////////////////////////////////////// CRAWLER ///////////////////////////////////////////////////

        // CrawlerService crawlerService = applicationContext.getBean(CrawlerService.class);
        // crawlerService.Crawl();

        //////////////////////////////////////////////////// INDEXER ///////////////////////////////////////////////////

        // IndexerService indexer = applicationContext.getBean(IndexerService.class);
        // indexer.main();

        //////////////////////////////////////////////////// RANKER ////////////////////////////////////////////////////

        // RankerService rankerService = applicationContext.getBean(RankerService.class);
        // rankerService.rankPages();

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }
}
