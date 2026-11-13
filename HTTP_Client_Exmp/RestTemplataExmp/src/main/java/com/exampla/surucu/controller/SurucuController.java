package com.exampla.surucu.controller;


import com.exampla.surucu.CarRequest;
import com.exampla.surucu.CarResponse;
import com.exampla.surucu.service.SurucuService;
import com.exampla.surucu.service.SurucuServiceWExchange;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/surucu")
@RestController
public class SurucuController {


    private final SurucuService surucuService;
    private final SurucuServiceWExchange surucuServiceWExchange;

    public SurucuController(SurucuService surucuService, SurucuServiceWExchange surucuServiceWExchange) {
        this.surucuService = surucuService;
        this.surucuServiceWExchange = surucuServiceWExchange;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCar(@PathVariable Integer id) {
        ResponseEntity<?> responseEntity = surucuServiceWExchange.getCars(id);
        return responseEntity;
    }

    @GetMapping()
    public ResponseEntity<List<CarResponse>> getCar(
            @RequestHeader HttpHeaders headers) {

        ResponseEntity<List<CarResponse>> responseEntity =
                surucuServiceWExchange.getCarList();

        headers.forEach((key, value) -> {
            System.out.println("header: " + key + " --> " + value);
        });

        return responseEntity;
    }

    //    @GetMapping("/info/{id}")
//    public ResponseEntity<?> getCarDetail(@PathVariable Integer id){
//            CarResponse carResponse= surucuService.getCarDetail(id);
//            return ResponseEntity.ok().body(carResponse);
//    }
    @GetMapping("/findbymodel")
    public ResponseEntity<?> getCarByModel(@RequestParam String model) {
        ResponseEntity<?> carResponse = surucuService.getCarByModel(model);
        return carResponse;
    }

    @GetMapping("/sum/{sayi1}/{sayi2}")
    public int toplam(
            @PathVariable Integer sayi1,
            @PathVariable Integer sayi2) {
        int top = surucuService.getTop(sayi1, sayi2);
        return top;
    }

    @GetMapping("/sumUri/{sayi1}/{sayi2}")
    public int toplamUri(
            @PathVariable Integer sayi1,
            @PathVariable Integer sayi2) {
        int top = surucuService.getToplamURI(sayi1, sayi2);
        return top;
    }

    @GetMapping("/toplam3")
    public int toplam3(@RequestParam Integer sayiA, @RequestParam Integer sayiB, @RequestParam Integer sayiC) {
        int toplam = surucuService.getToplam2(sayiA, sayiB, sayiC);
        return toplam;
    }

    @GetMapping("/toplam3Uri")
    public int toplam3Uri(@RequestParam Integer sayiA, @RequestParam Integer sayiB, @RequestParam Integer sayiC) {
        int toplam = surucuService.getToplam3(sayiA, sayiB, sayiC);
        return toplam;
    }
//    @GetMapping("/UseParameterizedType")
//    public ResponseEntity<List<CarResponse>> getAllCar() {
//        List<CarResponse> carResponse = SurucuService.getCarAll();
//        return ResponseEntity.ok(carResponse);
//    }

    @GetMapping("/forSurucuServiceWexchange/{id}")
    public ResponseEntity<CarResponse> getCar2(@PathVariable Integer id) {
        CarResponse car = surucuServiceWExchange.getCar(id);
        return ResponseEntity.ok(car);
    }

    @GetMapping("/forSurucuServiceWexchange/model")
    public ResponseEntity<CarResponse> getCar2(@RequestParam String model) {
        CarResponse car = surucuServiceWExchange.getCarByModel(model);
        return ResponseEntity.ok(car);
    }

    @GetMapping("/ardisikKelime/{adet}")
    public ResponseEntity<?> kelimeYazdir
            (
                    @PathVariable int adet
                    , @RequestParam String kelime
            ) {
        ResponseEntity<?> yazdir = surucuServiceWExchange.kelimeYazdir(kelime, adet);
        return yazdir;
    }

    @PostMapping("/addCar")
    public ResponseEntity<CarResponse> addCar(@Valid @RequestBody CarRequest carRequest) {
        CarResponse carResponse = surucuServiceWExchange.addCar(carRequest);
        return ResponseEntity.ok(carResponse);
    }

    @PostMapping("/addCar2")
    public ResponseEntity<?> addCar2(@Valid @RequestBody CarRequest carRequest) {
        ResponseEntity<?> responseEntity = surucuServiceWExchange.addCar2(carRequest);
        return responseEntity;
    }

    @PostMapping("/addCar3")
    public ResponseEntity<?> addCar3(@Valid @RequestBody CarRequest carRequest) {
        ResponseEntity<?> responseEntity = surucuServiceWExchange.addCar3(carRequest);
        return responseEntity;
    }

    @PostMapping("/addCarModel/{model}")
    public ResponseEntity<?> addCarWModel(
            @PathVariable String model,
            @RequestParam int fiyat,
            @Valid @RequestBody CarRequest carRequest) {

        ResponseEntity<?> responseEntity =
                surucuServiceWExchange.PostCarModel(model, fiyat, carRequest);

        return responseEntity;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCar(@PathVariable Integer id) {
        surucuServiceWExchange.DeleteCar(id);
        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/delete2/{id}")
    public ResponseEntity<?> deleteCar2(@PathVariable Integer id) {
        surucuServiceWExchange.DeleteWithExchange(id);
        return ResponseEntity.noContent().build();

    } 
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCar(@PathVariable Integer id,CarRequest carRequest) {
        ResponseEntity<?> responseEntity = surucuServiceWExchange.UpdateCar(id, carRequest);
        return ResponseEntity.ok().body(responseEntity);

    }

}
