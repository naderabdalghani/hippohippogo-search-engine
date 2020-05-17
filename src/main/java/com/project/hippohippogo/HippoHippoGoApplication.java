package com.project.hippohippogo;

import com.project.hippohippogo.controllers.CrawlerController;
import com.project.hippohippogo.repositories.CrawlerRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HippoHippoGoApplication {

    public static void main(String[] args) {
        CrawlerController C = new CrawlerController();
        C.insert();
        //SpringApplication.run(HippoHippoGoApplication.class, args);
    }

}
