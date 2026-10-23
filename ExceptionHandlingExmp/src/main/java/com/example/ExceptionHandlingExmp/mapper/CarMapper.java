package com.example.ExceptionHandlingExmp.mapper;

import com.example.ExceptionHandlingExmp.dto.CarRequest;
import com.example.ExceptionHandlingExmp.dto.CarResponse;
import com.example.ExceptionHandlingExmp.entity.Car;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel="spring")
public interface CarMapper {
    //Entity dönüşümü

    Car toCar(CarRequest carRequest);
    CarResponse toCarResponse(Car car);

    List<CarResponse> toCarResponseList(List<Car> cars);

}
