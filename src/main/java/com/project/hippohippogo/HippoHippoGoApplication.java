package com.project.hippohippogo;

import com.project.hippohippogo.services.Ranker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class HippoHippoGoApplication {
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(HippoHippoGoApplication.class, args);
        Ranker ranker = applicationContext.getBean(Ranker.class);
        //ranker.rankPages();
        ranker.getURLs("soccer");
    }

}
