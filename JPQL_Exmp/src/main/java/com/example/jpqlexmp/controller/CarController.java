package com.example.jpqlexmp.controller;

import com.example.jpqlexmp.dto.CarResponse;
import com.example.jpqlexmp.entity.Car;
import com.example.jpqlexmp.mapper.CarMapper;
import com.example.jpqlexmp.repository.CarRepository;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.print.DocFlavor;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("api/v1/cars")
public class CarController {

    private final CarRepository carRepository;
    private final CarMapper carMapper;

    public CarController(CarRepository carRepository, CarMapper carMapper) {
        this.carRepository = carRepository;
        this.carMapper = carMapper;
    }


    //pagination uygulama

    @GetMapping("/modelsirala")
    public ResponseEntity<?> pageandSearch(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String model
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Car> pageSaved = carRepository.findByModel(model, pageable);
        List<Car> content = pageSaved.getContent();
        List<CarResponse> carResponseList = carMapper.toCarResponseList(content);
        PageImpl<CarResponse> carResponses = new PageImpl<>(carResponseList, pageable, pageSaved.getTotalElements());
        return ResponseEntity.ok().body(carResponses);
    }

    @GetMapping("/field")
    public ResponseEntity<?> fieldandSearch(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sort,
            @RequestParam String field,
            @RequestParam String deger ) throws IllegalAccessException {
        Pageable pageable = PageRequest.of(page, size);
        Page<Car> pageSaved;
        switch (field) {
            case "fiyat": {
                pageSaved = carRepository.findByFiyat(Long.parseLong(deger), pageable);
                break;
            }
            case "yas": {
                 pageSaved = carRepository.findByYas(Integer.parseInt(deger),pageable);
                break;
            }
            case "marka": {
                 pageSaved = carRepository.findByMarka(deger, pageable);
                break;
            }
            case "model": {
                 pageSaved = carRepository.findByModel(deger, pageable);
                break;
            }
            default:{
                throw new IllegalAccessException("Invalid field: "+field);
            }
        }
        return ResponseEntity.ok(pageSaved);
    }
//    @GetMapping("/bilgi")
//    public ResponseEntity<Map<String, Object>> bilgi() {
//        Pageable pageable = PageRequest.of(0, 4);
//        Page<Car> page = carRepository.findAll(pageable);
//        Map<String, Object> stringMap = Map.of(
//                "toplam eleman sayısı", page.getTotalElements(),
//                "toplam sayfa sayısı", page.getTotalPages(),
//                "ŞUAN HANGİ SAYFA", page.getNumber(),
//                "son sayfa mı", page.isLast(),
//                "ilk sayfa mı", page.isFirst(),
//                "sayfadaki eleman sayısı", page.getNumberOfElements(),
//                "elemanlar",page.getContent()
//        );
//
//        return ResponseEntity.ok().body(stringMap);
//    }
//    @GetMapping("/teklisiralama")
//    public ResponseEntity<Page<Car>> getCar(
//            @RequestParam int pageNumber,
//            @RequestParam int pageSize,
//            @RequestParam String field,
//            @RequestParam String siralama
//    ) {
//        Sort sort = Sort.unsorted();
//        if (siralama.equals("desc")) {
//            sort = Sort.by(field).descending();
//        } else if (siralama.equals("asc")) {
//            sort = Sort.by(field).ascending();
//        }
//
//        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
//        Page<Car> page = carRepository.findAll(pageable);
//        return ResponseEntity.ok(page);
//    }
//
//    @GetMapping("/coklusiralama")
//    public ResponseEntity<Page<Car>> getCars() {
//        Sort sort = Sort.by("marka").ascending()
//                .and(Sort.by("yas").ascending());
//        Pageable pageable = PageRequest.of(0, 30, sort);
//        Page<Car> pageSiralama = carRepository.findAll(pageable);
//        return ResponseEntity.ok().body(pageSiralama);
//    }
//
//    @GetMapping("/listsiralama")
//    public ResponseEntity<Page<CarResponse>> getCarResponse(){
//        Pageable pageable=PageRequest.of(0,15,Sort.by("id").ascending());
//        Page<Car> page = carRepository.findAll(pageable);
//        List<Car> listCar= page.getContent();
//        List<CarResponse> carResponseList=carMapper.toCarResponseList(listCar);
//        PageImpl<CarResponse> carResponses = new PageImpl<>(carResponseList, pageable, page.getTotalElements());
//        return ResponseEntity.ok(carResponses);
//    }

}
