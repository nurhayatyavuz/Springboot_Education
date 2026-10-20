package com.example.jpqlexmp.repository;

import com.example.jpqlexmp.entity.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarRepository extends JpaRepository<Car,Integer> {

    Optional<Car> findByModel(String model);
    //sadece 100 arabayı almak istersek
    Page <Car> findByModel(String model, Pageable pageable);

    Page<Car> findByYas(Integer yas, Pageable pageable);

    Page<Car> findByFiyat(Long fiyat, Pageable pageable);

    Page<Car> findByMarka(String marka, Pageable pageable);
}
