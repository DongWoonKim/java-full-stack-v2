package com.example.spring.catalogservice.logger;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

public class DataSourceUrlLoggerInitializer  implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        String datasourceUrl = environment.getProperty("spring.datasource.url");
        System.out.println("Datasource URL: " + datasourceUrl);
    }
}
