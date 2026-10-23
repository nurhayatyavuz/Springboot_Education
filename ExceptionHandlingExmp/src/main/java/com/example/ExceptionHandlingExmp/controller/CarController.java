package com.example.ExceptionHandlingExmp.controller;


import com.example.ExceptionHandlingExmp.dto.CarRequest;
import com.example.ExceptionHandlingExmp.dto.CarResponse;
import com.example.ExceptionHandlingExmp.mapper.CarMapper;
import com.example.ExceptionHandlingExmp.service.CarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/car/v1")
public class CarController {

    public CarController(CarMapper carMapper, CarService carService) {
        this.carMapper = carMapper;
        this.carService=carService;
    }

    private final CarMapper carMapper;
    private final CarService carService;

    @PostMapping
    public ResponseEntity<CarResponse> addCar(@RequestBody CarRequest carRequest){
        CarResponse carResponse=carService.addCar(carRequest);
        return ResponseEntity.ok().body(carResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarResponse> getCar(@PathVariable Integer id){
        CarResponse carResponse=carService.getCarById(id);
        return ResponseEntity.ok().body(carResponse);
    }

    @GetMapping
    public ResponseEntity<List<CarResponse>> getAllCar() {
        List<CarResponse> carResponse = carService.getCarAll();
        return ResponseEntity.ok(carResponse);
    }
    @PutMapping("/{id}")
    public ResponseEntity<CarResponse> updateCar(
            @PathVariable Integer id,
            @RequestBody CarRequest carRequest) {

        CarResponse carResponse = carService.updateCar(id, carRequest);
        return ResponseEntity.ok().body(carResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Integer id) {

        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }


}
