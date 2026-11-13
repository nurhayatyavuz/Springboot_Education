package com.exampla.surucu.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class ConfigProperty {

    @Bean //IoC conteinara eklemek için önemli bean tanımı.
    public RestTemplate restTemplate() {
        RestTemplate restTemplate =new RestTemplate();
         restTemplate.getInterceptors().add(new CustomIntercepter());

        return restTemplate;
    }
}
