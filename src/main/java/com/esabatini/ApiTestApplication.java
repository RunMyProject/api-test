package com.esabatini;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
// import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

// @EnableMongoRepositories
@SpringBootApplication(scanBasePackages = { "com.esabatini" })
@EnableAsync
public class ApiTestApplication {

    @Bean
    protected RestTemplate getTemplate() {
        return new RestTemplate();
    }
    
    public static void main(String[] args) {
        new SpringApplicationBuilder(ApiTestApplication.class).run(args);
    }

}
