package com.example.carsdetailsmicroservice.mapper;

import com.example.carsdetailsmicroservice.dto.car.create.CarCreateRequestDto;
import com.example.carsdetailsmicroservice.dto.car.get.CarGetResponseDto;
import com.example.carsdetailsmicroservice.entity.Car;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * This interface represents a mapper for converting between Car objects and DTOs.
 */
@Mapper
public interface CarMapper {
  CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);

  CarCreateRequestDto toDtoRequest(Car car);

  Car toCar(CarCreateRequestDto carCreateRequestDto);

  CarGetResponseDto toDtoResponse(Car car);

}