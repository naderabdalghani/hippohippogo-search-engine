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
        RankerService rankerService = applicationContext.getBean(RankerService.class);
        List<Integer> pagesIDs = rankerService.getPageURLs("football soccer",null,"192.168.1.1");
        IndexerService a = applicationContext.getBean(IndexerService.class);
        a.main();
    }

    private void initialize(CrawlerService crawlerService,RankerService rankerService) {
        crawlerService.Crawl();
        rankerService.rankPages();
    }
}
