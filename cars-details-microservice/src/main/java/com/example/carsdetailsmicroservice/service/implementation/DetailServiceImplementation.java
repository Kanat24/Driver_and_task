package com.example.carsdetailsmicroservice.service.implementation;

import com.example.carsdetailsmicroservice.dto.detail.get.DetailGetResponseDto;
import com.example.carsdetailsmicroservice.dto.detail.update.DetailUpdateRequestDto;
import com.example.carsdetailsmicroservice.entity.Detail;
import com.example.carsdetailsmicroservice.exceptions.ErrorCode;
import com.example.carsdetailsmicroservice.exceptions.detail.DetailAlreadyExistException;
import com.example.carsdetailsmicroservice.exceptions.detail.DetailNotFoundException;
import com.example.carsdetailsmicroservice.mapper.DetailMapper;
import com.example.carsdetailsmicroservice.repository.DetailRepository;
import com.example.carsdetailsmicroservice.service.DetailService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class DetailServiceImplementation implements DetailService {
  private final DetailRepository detailRepository;

  @Autowired
  public DetailServiceImplementation(DetailRepository detailRepository) {
    this.detailRepository = detailRepository;
  }


  @Override
  public void createDetail(Detail detail) {
    if (detailRepository.existsBySerialNumber(detail.getSerialNumber())) {
      throw new DetailAlreadyExistException(ErrorCode.DETAIL_ALREADY_EXIST);
    }
    detailRepository.save(detail);
  }

  @Override
  public Page<DetailGetResponseDto> getAllDetails(int page, int size, String sortBy) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
    Page<Detail> detailPage = detailRepository.findAll(pageable);
    return detailPage.map(DetailMapper.INSTANCE::toDtoResponse);
  }

  @Override
  public Optional<DetailGetResponseDto> findDetailBySerialNumber(String serialNumber) {
    return detailRepository.findBySerialNumber(serialNumber)
                .map(DetailMapper.INSTANCE::toDtoResponse)
                .map(Optional::ofNullable)
                .orElseThrow(() -> new DetailNotFoundException(ErrorCode.DETAIL_NOT_FOUND));
  }

  @Override
  public Page<DetailGetResponseDto> searchDetailsBySerialNumber(String serialNumber,
                                                                int page,
                                                                int size,
                                                                String sortBy) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
    Page<Detail> detailPage = detailRepository.findBySerialNumberContaining(serialNumber, pageable);
    return detailPage.map(DetailMapper.INSTANCE::toDtoResponse);
  }

  @Override
  public Page<DetailGetResponseDto> searchDetailsByPrice(BigDecimal price,
                                                         int page,
                                                         int size,
                                                         String sortBy) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
    Page<Detail> detailPage = detailRepository.findByPrice(price, pageable);
    return detailPage.map(DetailMapper.INSTANCE::toDtoResponse);
  }

  @Transactional
  @Override
  public void deleteDetail(String serialNumber) {
    if (!detailRepository.existsBySerialNumber(serialNumber)) {
      throw new DetailNotFoundException(ErrorCode.DETAIL_NOT_FOUND);
    }
    detailRepository.deleteBySerialNumber(serialNumber);
  }

  @Override
  public Detail changeDetail(String serialNumber,
                               DetailUpdateRequestDto detailUpdateRequestDtoCar) {
    if (!detailRepository.existsBySerialNumber(serialNumber)) {
      throw new DetailNotFoundException(ErrorCode.DETAIL_NOT_FOUND);
    }
    Detail detail = detailRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new DetailNotFoundException(ErrorCode.DETAIL_NOT_FOUND));
    detail.setSerialNumber(Objects.requireNonNullElse(detailUpdateRequestDtoCar.getSerialNumber(),
                                                      detail.getSerialNumber()));
    detail.setPrice(Objects.requireNonNullElse(detailUpdateRequestDtoCar.getPrice(),
                                               detail.getPrice()));
    return detailRepository.save(detail);
  }
}