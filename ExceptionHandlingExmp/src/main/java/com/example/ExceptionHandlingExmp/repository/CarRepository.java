package com.example.ExceptionHandlingExmp.repository;


import com.example.ExceptionHandlingExmp.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarRepository extends JpaRepository<Car,Integer> {

    Optional<Car> findByModel(String model);
}
