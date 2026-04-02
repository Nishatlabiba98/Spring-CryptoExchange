package com.zipcoder.cryptonator_api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }


    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public CommandLineRunner run(RestTemplate restTemplate) {
        return args -> {
            this.sampleFetch(restTemplate);
        };
    }

    private void sampleFetch(RestTemplate restTemplate) {
        String uriString = "https://api.cryptonator.com/api/ticker/btc-usd";
        System.out.println("Fetching object from cryptonator.com");
        System.out.println("Attempting to retrieve object from URI...");
        System.out.println(uriString);
        Object jsonObject = restTemplate.getForObject(uriString, Object.class);
        System.out.println(jsonObject.toString());
    }
}