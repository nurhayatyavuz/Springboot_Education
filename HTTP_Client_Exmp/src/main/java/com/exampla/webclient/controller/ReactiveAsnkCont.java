package com.exampla.webclient.controller;

import com.exampla.webclient.dto.CarRequest;
import com.exampla.webclient.dto.CarResponse;
import com.exampla.webclient.service.ReactiveAsnk;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/reactive")
public class ReactiveAsnkCont {

    public ReactiveAsnkCont(ReactiveAsnk reactiveAsnk) {
        this.reactiveAsnk = reactiveAsnk;
    }

    private final ReactiveAsnk reactiveAsnk;

    @GetMapping("/{id}")
    public Mono<CarResponse> getCar(@PathVariable Integer id){
        Mono<CarResponse> car = reactiveAsnk.getCar(id);
        return car;
    }
    // GET - Bütün araçları getir
    @GetMapping
    public Flux<CarResponse> getAllCars() {

        Flux<CarResponse> allCars = reactiveAsnk.getAllCars();
        return allCars;

    }


    @PostMapping
    public Mono<CarResponse> createCar(
            @RequestBody CarRequest carRequest) {

        Mono<CarResponse> car = reactiveAsnk.createCar(carRequest);
        return  car;
    }

    // PUT - Araç güncelle
    @PutMapping("/{id}")
    public Mono<CarResponse> updateCar(
            @PathVariable Integer id,
            @RequestBody CarRequest carRequest) {

        Mono<CarResponse> carResponseMono = reactiveAsnk.updateCar(id, carRequest);
        return carResponseMono;
    }


    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCar(@PathVariable Integer id) {

        Mono<ResponseEntity<Void>> responseEntityMono = reactiveAsnk.deleteCar(id);
        return responseEntityMono;
    }
    @GetMapping("/carmodel")
    public Mono<CarResponse> getCarsByModel(
            @RequestParam String model) {


        Mono<CarResponse> carsByModel = reactiveAsnk.getCarsByModel(model);

        return carsByModel;
    }
}
