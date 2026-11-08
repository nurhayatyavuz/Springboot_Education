package com.exampla.surucu.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.reactive.ClientHttpRequest;

import java.io.IOException;

import static java.lang.Math.log;

@Slf4j
public class CustomIntercepter implements ClientHttpRequestInterceptor {

        @Override
        public ClientHttpResponse intercept(
                HttpRequest request,
                byte[] body,
                ClientHttpRequestExecution execution
        ) throws IOException {

            request.getHeaders().add("X-Private-Info", "gizli bilgi");

            CustomIntercepter.log.info("İstek URI: {}", request.getURI());
            log.info("HTTP Method: {}", request.getMethod());
            log.info("Headers: {}", request.getHeaders());

            return execution.execute(request, body);
        }
    }

