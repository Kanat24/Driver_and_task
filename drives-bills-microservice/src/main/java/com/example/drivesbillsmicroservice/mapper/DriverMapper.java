package com.example.drivesbillsmicroservice.mapper;

import com.example.drivesbillsmicroservice.dto.driver.create.DriverCreateRequestDto;
import com.example.drivesbillsmicroservice.dto.driver.get.DriverGetResponseDto;
import com.example.drivesbillsmicroservice.entity.Driver;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper interface for converting Driver objects to DTOs and vice versa.
 */
@Mapper
public interface DriverMapper {
  DriverMapper INSTANCE = Mappers.getMapper(DriverMapper.class);

  DriverCreateRequestDto toDtoRequest(Driver driver);

  Driver toDriver(DriverCreateRequestDto driverCreateRequestDto);

  DriverGetResponseDto toDtoResponse(Driver driver);

  Driver toDriver(DriverGetResponseDto driverGetResponseDto);
}