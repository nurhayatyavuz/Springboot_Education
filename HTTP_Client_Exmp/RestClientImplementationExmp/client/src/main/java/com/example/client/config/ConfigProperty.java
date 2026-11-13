package com.example.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClient;

@Configuration
public class ConfigProperty {

    @Bean
    public RestClient restClient() {
        return RestClient
                .builder()
                .defaultHeader("Accept", "application/json")
                .baseUrl("http://localhost:8080")
                .build();
    }
}