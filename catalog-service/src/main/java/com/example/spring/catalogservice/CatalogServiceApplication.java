package com.example.spring.catalogservice;

import com.example.spring.catalogservice.logger.DataSourceUrlLoggerInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CatalogServiceApplication {

    public static void main(String[] args) {
//        SpringApplication.run(CatalogServiceApplication.class, args);
        SpringApplication app = new SpringApplication(CatalogServiceApplication.class);
        app.addInitializers(new DataSourceUrlLoggerInitializer());
        app.run(args);
    }

}
