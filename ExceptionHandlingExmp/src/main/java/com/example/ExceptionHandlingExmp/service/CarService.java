package com.example.ExceptionHandlingExmp.service;


import com.example.ExceptionHandlingExmp.dto.CarRequest;
import com.example.ExceptionHandlingExmp.dto.CarResponse;
import com.example.ExceptionHandlingExmp.entity.Car;
import com.example.ExceptionHandlingExmp.exception.CarAlreadyExistException;
import com.example.ExceptionHandlingExmp.exception.CarNotFoundException;
import com.example.ExceptionHandlingExmp.mapper.CarMapper;
import com.example.ExceptionHandlingExmp.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {


    public CarService(CarRepository carRepository, CarMapper carMapper) {
        this.carRepository = carRepository;
        this.carMapper=carMapper;
    }

    private final CarRepository carRepository;
    private final CarMapper carMapper;


    public CarResponse addCar(CarRequest carRequest) {

       Optional<Car> car=carRepository.findByModel(carRequest.getModel());
       if(car.isPresent()){
           throw new CarAlreadyExistException("Car already exists");
       }
       else{
           Car car1=carMapper.toCar(carRequest);
           Car savedCar=carRepository.save(car1);

           CarResponse carResponse = carMapper.toCarResponse(savedCar);

           return carResponse;
       }
    }

    public CarResponse getCarById(Integer id) {

        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car not found ID: " + id));

        CarResponse carResponse=carMapper.toCarResponse(car);
        return carResponse;

    }

    public List<CarResponse> getCarAll() {

       List<Car> cars=carRepository.findAll();

       List<CarResponse> carResponsesList=carMapper.toCarResponseList(cars);
       return carResponsesList;
    }

    public CarResponse updateCar(Integer id, CarRequest carRequest) {

        Car car=carRepository.findById(id).orElseThrow(()-> new CarNotFoundException("car not found"));
        car.setFiyat(carRequest.getFiyat());
        car.setYas(carRequest.getYas());

        Car savedCar=carRepository.save(car);
        CarResponse carResponse=carMapper.toCarResponse(savedCar);
        return  carResponse;

    }

    public void deleteCar(Integer id) {

      Car car=carRepository.findById(id).orElseThrow(() -> new CarNotFoundException("car not found"));
      carRepository.deleteById(id);
    }
}
