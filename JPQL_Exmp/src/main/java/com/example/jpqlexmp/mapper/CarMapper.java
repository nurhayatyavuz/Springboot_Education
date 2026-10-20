package com.example.jpqlexmp.mapper;

import com.example.jpqlexmp.dto.CarRequest;
import com.example.jpqlexmp.dto.CarResponse;
import com.example.jpqlexmp.entity.Car;
import org.mapstruct.Mapper;

import java.sql.ClientInfoStatus;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CarMapper {

    Car toCAr(CarRequest carRequest);
    CarResponse toCarResponse(Car car);
    List<CarResponse> toCarResponseList(List<Car> cars);
//    Car toEntity(CarDto carDto);
    //CarDto toDto(Car car);
    //@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    //Car partialUpdate(CarDto carDto, @MappingTarget Car car);
}
