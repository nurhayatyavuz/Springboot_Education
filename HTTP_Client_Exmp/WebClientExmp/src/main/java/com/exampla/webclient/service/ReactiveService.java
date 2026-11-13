package com.exampla.webclient.service;


import com.exampla.webclient.dto.CarRequest;
import com.exampla.webclient.dto.CarResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class ReactiveService {

    public ReactiveService(WebClient webClient) {
        this.webClient = webClient;
    }

    private final WebClient webClient;


    public ResponseEntity<CarResponse> getCar(Integer id){
        CarResponse block = webClient
                .get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(CarResponse.class)
                .block();

        return ResponseEntity.ok(block);
    }
    public ResponseEntity<List<CarResponse>> getAllCars() {

        List<CarResponse> carResponses = webClient
                .get()
                .uri("")
                .retrieve()
                .bodyToFlux(CarResponse.class)
                .collectList()
                .block();

        return ResponseEntity.ok(carResponses);
    }
    // GET - Modele göre araç getir
    public ResponseEntity<CarResponse> getCarsByModel(String model) {

        CarResponse carResponses = webClient
                .get()
                .uri("/carmodel?model={model}",model)
                .retrieve()
                .bodyToMono(CarResponse.class)
                .block();

        return ResponseEntity.ok(carResponses);
    }

    // 3. POST - Yeni araç oluştur
    public ResponseEntity<CarResponse> createCar(CarRequest carRequest) {

        CarResponse carResponse = webClient
                .post()
                .uri("")
                .bodyValue(carRequest)
                .retrieve()
                .bodyToMono(CarResponse.class)
                .block();

        return ResponseEntity.status(HttpStatus.CREATED).body(carResponse);
    }

    // 4. PUT - Araç güncelle
    public ResponseEntity<CarResponse> updateCar(Integer id, CarRequest carRequest) {

        CarResponse carResponse = webClient
                .put()
                .uri("/{id}", id)
                .bodyValue(carRequest)
                .retrieve()
                .bodyToMono(CarResponse.class)
                .block();

        return ResponseEntity.ok(carResponse);
    }

    // 5. DELETE - Araç sil
    public ResponseEntity<Void> deleteCar(Integer id) {

        webClient
                .delete()
                .uri("/{id}", id)
                .retrieve()
                .toBodilessEntity()
                .block();

        return ResponseEntity.noContent().build();
    }
}


