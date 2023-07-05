package com.example.carsdetailsmicroservice.mapper;

import com.example.carsdetailsmicroservice.dto.detail.create.DetailCreateRequestDto;
import com.example.carsdetailsmicroservice.dto.detail.get.DetailGetResponseDto;
import com.example.carsdetailsmicroservice.entity.Detail;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * This interface represents a mapper for converting between Detail objects and DTOs.
 */
@Mapper
public interface DetailMapper {
  DetailMapper INSTANCE = Mappers.getMapper(DetailMapper.class);

  DetailGetResponseDto toDtoResponse(Detail detail);

  Detail toDetail(DetailCreateRequestDto detailCreateRequestDto);
}