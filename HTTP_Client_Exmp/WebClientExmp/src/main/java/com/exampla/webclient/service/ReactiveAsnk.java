package com.exampla.webclient.service;

import com.exampla.webclient.dto.CarRequest;
import com.exampla.webclient.dto.CarResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ReactiveAsnk {

    public ReactiveAsnk(WebClient webClient) {
        this.webClient = webClient;
    }

    private final WebClient webClient;

    public Mono<CarResponse> getCar(Integer id) {
        Mono<CarResponse> block = webClient
                .get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(CarResponse.class);

        return block;
    }

    public Flux<CarResponse> getAllCars() {

        Flux<CarResponse> listFlux = webClient
                .get()
                .uri("")
                .retrieve()
                .bodyToFlux(CarResponse.class);

        return listFlux;
    }


    public Mono<CarResponse> getCarsByModel(String model) {

        Mono<CarResponse> carResponseMono = webClient
                .get()
                .uri("/carmodel?model={model}", model)
                .retrieve()
                .bodyToMono(CarResponse.class);

        return carResponseMono;
    }

    public Mono<CarResponse> createCar(CarRequest carRequest) {

        Mono<CarResponse> carResponseMono = webClient
                .post()
                .uri("")
                .bodyValue(carRequest)
                .retrieve()
                .bodyToMono(CarResponse.class);

        return carResponseMono;
    }


    public Mono<CarResponse> updateCar(Integer id, CarRequest carRequest) {

        Mono<CarResponse> carResponseMono = webClient
                .put()
                .uri("/{id}", id)
                .bodyValue(carRequest)
                .retrieve()
                .bodyToMono(CarResponse.class);

        return carResponseMono;
    }


    public Mono<ResponseEntity<Void>> deleteCar(Integer id) {

        Mono<ResponseEntity<Void>> bodilessEntity = webClient
                .delete()
                .uri("/{id}", id)
                .retrieve()
                .toBodilessEntity();

        return bodilessEntity;
    }


}
