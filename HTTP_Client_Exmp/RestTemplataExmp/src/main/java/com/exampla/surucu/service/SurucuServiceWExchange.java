package com.exampla.surucu.service;

import com.exampla.surucu.CarRequest;
import com.exampla.surucu.CarResponse;
import com.exampla.surucu.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.micrometer.observation.autoconfigure.ObservationProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.service.registry.HttpServiceGroup;
import org.springframework.web.util.UriComponentsBuilder;
import tools.jackson.databind.ObjectMapper;

import java.lang.reflect.ParameterizedType;
import java.net.URI;
import java.util.List;

@Slf4j
@Service
public class SurucuServiceWExchange {
    public SurucuServiceWExchange(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private final RestTemplate restTemplate;


    //referans
//    @GetMapping("/{id}")
//    public ResponseEntity<CarResponse> getCar(@PathVariable @Min(1) Integer id){
//        CarResponse carResponse=carService.getCarById(id);
//        return ResponseEntity.ok().body(carResponse);
//    }


    public CarResponse getCar(Integer id) {
        String path = "http://localhost:8081/api/car/v1/{id}";
        //w UriComponent
        URI uri = UriComponentsBuilder
                .fromUriString(path)
                .buildAndExpand(id)
                .toUri();

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<CarResponse> exchange = restTemplate.exchange(
                //path,
                uri,
                HttpMethod.GET,
                requestEntity,
                CarResponse.class
        );
        return exchange.getBody();
    }

    //w request param
    public CarResponse getCarByModel(String model) {

        String path = "http://localhost:8081/api/car/v1/carmodel";

        URI uri = UriComponentsBuilder
                .fromUriString(path)
                .queryParam("model", model)
                .build()
                .encode()
                .toUri();

        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<CarResponse> exchange = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                requestEntity,
                CarResponse.class
        );

        return exchange.getBody();
    }

    public ResponseEntity<String> kelimeYazdir(String kelime, Integer adet) {
        try {
            String path = "http://localhost:8081/api/car/v1/kelime/{adet}";
            URI uri = UriComponentsBuilder
                    .fromUriString(path)
                    .queryParam("kelime", kelime)
                    .buildAndExpand(adet)
                    .toUri();

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    String.class
            );
            return responseEntity;

        } catch (HttpClientErrorException ex) {
            // 4xx errors
            String body = ex.getResponseBodyAsString();
            HttpStatusCode statusCode = ex.getStatusCode();
            return ResponseEntity.status(statusCode).body(body);
        } catch (HttpServerErrorException ex) {
            // 5xx errors
            String body = ex.getResponseBodyAsString();
            HttpStatusCode statusCode = ex.getStatusCode();
            return ResponseEntity.status(statusCode).body(body);

        } catch (ResourceAccessException ex) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("sunucu hatası: " + ex.getMessage());
        }
    }

    public ResponseEntity<?> getCars(Integer id) {
        try {
            ResponseEntity<CarResponse> exchange = restTemplate.exchange(
                    "http://localhost:8081/api/car/v1/{id}",
                    HttpMethod.GET,
                    null,
                    CarResponse.class,
                    id
            );
            return exchange;
        } catch (HttpClientErrorException ex) {
            // 4xx
            String body = ex.getResponseBodyAsString();

            ObjectMapper objectMapper = new ObjectMapper();
            ErrorResponse errorResponse =
                    objectMapper.readValue(body, ErrorResponse.class);

            log.error("hata oluştu {}", errorResponse.getMessage());

            HttpStatusCode statusCode = ex.getStatusCode();

            return ResponseEntity.status(statusCode).body(errorResponse);
        }
    }

    public ResponseEntity<List<CarResponse>> getCarList() {

        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<CarResponse>> exchange = restTemplate.exchange(
                "http://localhost:8081/api/car/v1",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<CarResponse>>() {}
        );

        return exchange;
    }

    public CarResponse addCar(CarRequest carRequest) {
        String path = "http://localhost:8081/api/car/v1";
        CarResponse carResponse = restTemplate.postForObject(
                path,
                carRequest,
                CarResponse.class
        );
        return carResponse;
    }

    public ResponseEntity<CarResponse> addCar2(CarRequest carRequest) {
        String path = "http://localhost:8081/api/car/v1";
        ResponseEntity<CarResponse> carResponseResponseEntity = restTemplate.postForEntity(
                path,
                carRequest,
                CarResponse.class
        );
        return carResponseResponseEntity; //geriye entitiy döndürüyor
    }

    public ResponseEntity<?> addCar3(CarRequest carRequest) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON); // postta setContentType methodu devreye girer
            HttpEntity<CarRequest> carRequestHttpEntity = new HttpEntity<>(carRequest, headers);
            String path = "http://localhost:8081/api/car/v1";
            ResponseEntity<CarResponse> exchange = restTemplate.exchange(
                    path,
                    HttpMethod.POST,
                    carRequestHttpEntity,
                    CarResponse.class);
            return exchange;
        } catch (HttpClientErrorException ex) {
            // 4xx
            String body = ex.getResponseBodyAsString();

            ObjectMapper objectMapper = new ObjectMapper();
            ErrorResponse errorResponse =
                    objectMapper.readValue(body, ErrorResponse.class);

            log.error("hata oluştu {}", errorResponse.getMessage());

            HttpStatusCode statusCode = ex.getStatusCode();

            return ResponseEntity.status(statusCode).body(errorResponse);
        }
    }

    //post mapping car model için
    public ResponseEntity<?> PostCarModel(
            String model,
            int fiyat,
            CarRequest carRequest) {

        try {
            URI uri = UriComponentsBuilder
                    .fromUriString("http://localhost:8081/api/car/v1/add/{model}")
                    .queryParam("fiyat", fiyat)
                    .buildAndExpand(model)
                    .toUri();

            HttpEntity<CarRequest> requestEntity =
                    new HttpEntity<>(carRequest);

            ResponseEntity<CarResponse> exchange = restTemplate.exchange(
                    uri,
                    HttpMethod.POST,
                    requestEntity,
                    CarResponse.class
            );

            return exchange;

        } catch (HttpClientErrorException ex) {

            String body = ex.getResponseBodyAsString();

            ObjectMapper objectMapper = new ObjectMapper();

            ErrorResponse errorResponse =
                    objectMapper.readValue(body, ErrorResponse.class);

            log.error("hata oluştu {}", errorResponse.getMessage());

            HttpStatusCode statusCode = ex.getStatusCode();

            return ResponseEntity.status(statusCode).body(errorResponse);
        }
    }

    public ResponseEntity<?> DeleteCar(Integer id) {
        try {
            String path = ("http://localhost:8081/api/car/v1/{id}");
            URI uri = UriComponentsBuilder
                    .fromUriString(path)
                    .buildAndExpand(id)
                    .toUri();

            restTemplate.delete(uri);
            return null;
        } catch (HttpClientErrorException ex) {
            String body = ex.getResponseBodyAsString();

            ObjectMapper objectMapper = new ObjectMapper();

            ErrorResponse errorResponse =
                    objectMapper.readValue(body, ErrorResponse.class);

            log.error("hata oluştu {}", errorResponse.getMessage());

            HttpStatusCode statusCode = ex.getStatusCode();

            return ResponseEntity.status(statusCode).body(errorResponse);
        }

    }

    public ResponseEntity<?> DeleteWithExchange(Integer id) {
        try {
            String path = ("http://localhost:8081/api/car/v1/{id}");
            URI uri = UriComponentsBuilder
                    .fromUriString(path)
                    .buildAndExpand(id)
                    .toUri();

            ResponseEntity<Void> exchange = restTemplate.exchange(
                    uri,
                    HttpMethod.DELETE,
                    null,
                    // silme işleminde geriye bir şey dönmez ->
                    Void.class

            );
            return exchange;
        } catch (
                HttpClientErrorException ex) {
            String body = ex.getResponseBodyAsString();

            ObjectMapper objectMapper = new ObjectMapper();

            ErrorResponse errorResponse =
                    objectMapper.readValue(body, ErrorResponse.class);

            log.error("hata oluştu {}", errorResponse.getMessage());

            HttpStatusCode statusCode = ex.getStatusCode();

            return ResponseEntity.status(statusCode).body(errorResponse);
        }
    }

    public ResponseEntity<?> UpdateCar(Integer id, CarRequest carRequest) {
        try {
            String path = ("http://localhost:8081/api/car/v1/{id}");
            URI uri = UriComponentsBuilder
                    .fromUriString(path)
                    .buildAndExpand(id)
                    .toUri();

            restTemplate.put(uri, carRequest);
            return  ResponseEntity.ok().body("güncellendi");

        } catch (
                HttpClientErrorException ex) {
            String body = ex.getResponseBodyAsString();

            ObjectMapper objectMapper = new ObjectMapper();

            ErrorResponse errorResponse =
                    objectMapper.readValue(body, ErrorResponse.class);

            log.error("hata oluştu {}", errorResponse.getMessage());

            HttpStatusCode statusCode = ex.getStatusCode();

            return ResponseEntity.status(statusCode).body(errorResponse);
        }
    }
    public ResponseEntity<?> UpdateCarWithExchange(Integer id, CarRequest carRequest) {
        try {
            String path = ("http://localhost:8081/api/car/v1/{id}");
            HttpEntity<CarRequest> httpEntity=new HttpEntity<>(carRequest);



            URI uri = UriComponentsBuilder
                    .fromUriString(path)
                    .buildAndExpand(id)
                    .toUri();

            ResponseEntity<CarResponse> exchange = restTemplate.exchange(
                    uri,
                    HttpMethod.PUT,
                    httpEntity,
                    CarResponse.class
            );
            return  exchange;

        } catch (
                HttpClientErrorException ex) {
            String body = ex.getResponseBodyAsString();

            ObjectMapper objectMapper = new ObjectMapper();

            ErrorResponse errorResponse =
                    objectMapper.readValue(body, ErrorResponse.class);

            log.error("hata oluştu {}", errorResponse.getMessage());

            HttpStatusCode statusCode = ex.getStatusCode();

            return ResponseEntity.status(statusCode).body(errorResponse);
        }
    }
}




