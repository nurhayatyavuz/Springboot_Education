package com.exampla.webclient.controller;

import com.exampla.webclient.dto.CarRequest;
import com.exampla.webclient.dto.CarResponse;
import com.exampla.webclient.service.ReactiveService;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/webclient")
public class ReactiveController {

    public ReactiveController(ReactiveService reactiveService) {
        this.reactiveService = reactiveService;
    }

    private final ReactiveService reactiveService;


    @GetMapping("/{id}")
    public ResponseEntity<CarResponse> getCar(@PathVariable Integer id){
        ResponseEntity<CarResponse> carResponse=reactiveService.getCar(id);
        return carResponse;
    }
    // GET - Bütün araçları getir
    @GetMapping
    public ResponseEntity<List<CarResponse>> getAllCars() {

        return reactiveService.getAllCars();
    }

    // POST - Yeni araç oluştur
    @PostMapping
    public ResponseEntity<CarResponse> createCar(
            @RequestBody CarRequest carRequest) {

        return reactiveService.createCar(carRequest);
    }

    // PUT - Araç güncelle
    @PutMapping("/{id}")
    public ResponseEntity<CarResponse> updateCar(
            @PathVariable Integer id,
            @RequestBody CarRequest carRequest) {

        return reactiveService.updateCar(id, carRequest);
    }

    // DELETE - Araç sil
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Integer id) {

        return reactiveService.deleteCar(id);
    }
    @GetMapping("/carmodel")
    public ResponseEntity<CarResponse> getCarsByModel(
            @RequestParam String model) {

        ResponseEntity<CarResponse> carsByModel =
                reactiveService.getCarsByModel(model);

        return carsByModel;
    }


}


