package com.project.hippohippogo;

import com.project.hippohippogo.services.CrawlerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class HippoHippoGoApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(HippoHippoGoApplication.class, args);
        CrawlerService crawlerService = applicationContext.getBean(CrawlerService.class);
        crawlerService.insert();
    }

}
