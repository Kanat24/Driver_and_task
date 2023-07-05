package com.example.drivesbillsmicroservice.service.implementation;

import com.example.drivesbillsmicroservice.dto.driver.create.DriverCreateRequestDto;
import com.example.drivesbillsmicroservice.dto.driver.get.DriverGetResponseDto;
import com.example.drivesbillsmicroservice.dto.driver.update.DriverUpdateRequestDto;
import com.example.drivesbillsmicroservice.entity.Driver;
import com.example.drivesbillsmicroservice.events.CarPurchaseEvent;
import com.example.drivesbillsmicroservice.events.DetailAddEvent;
import com.example.drivesbillsmicroservice.exceptions.ErrorCode;
import com.example.drivesbillsmicroservice.exceptions.driver.DriverAlreadyExistException;
import com.example.drivesbillsmicroservice.exceptions.driver.DriverNotFoundException;
import com.example.drivesbillsmicroservice.kafka.Producer;
import com.example.drivesbillsmicroservice.mapper.DriverMapper;
import com.example.drivesbillsmicroservice.repository.DriverRepository;
import com.example.drivesbillsmicroservice.service.DriverService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
public class DriverServiceImplementation implements DriverService {
  private final DriverRepository driverRepository;
  private final Producer producer;

  @Autowired
  public DriverServiceImplementation(DriverRepository driverRepository, Producer producer) {
    this.driverRepository = driverRepository;
    this.producer = producer;
  }


  @Scheduled(cron = "0 00 14 * * ?")
  public void congratulateDriversWithBirthday() {
    List<Driver> driversWithBirthday = driverRepository.findDriversWithBirthdayToday();
    for (Driver driver : driversWithBirthday) {
      System.out.println("Happy Birthday, " + driver.getFirstName() + " " + driver.getLastName());
    }
  }

  @Override
  public void registerDriver(DriverCreateRequestDto driverCreateRequestDto) {
    if (driverRepository.existsByPassport(driverCreateRequestDto.getPassport())) {
      throw new DriverAlreadyExistException(ErrorCode.DRIVER_ALREADY_EXIST);
    }
    driverRepository.save(DriverMapper.INSTANCE.toDriver(driverCreateRequestDto));
  }

  @Override
  public Optional<DriverGetResponseDto> findDriverByPassport(String passport) {
    Optional<Driver> driver = driverRepository.findDriverByPassport(passport);
    if (driver.isEmpty()) {
      throw new DriverNotFoundException(ErrorCode.DRIVER_NOT_FOUND);
    }
    return driver.map(DriverMapper.INSTANCE::toDtoResponse);
  }

  @Transactional
  @Override
  public void deleteDriver(String passport) {
    Optional<Driver> driver = driverRepository.findDriverByPassport(passport);
    if (driver.isEmpty()) {
      throw new DriverNotFoundException(ErrorCode.DRIVER_NOT_FOUND);
    }
    driverRepository.deleteByPassport(passport);
  }

  @Override
  @Transactional
  public void updateDriver(String passport, DriverUpdateRequestDto updatedDriver) {
    Driver driver = driverRepository.findDriverByPassport(passport)
                .orElseThrow(() -> new DriverNotFoundException(ErrorCode.DRIVER_NOT_FOUND));
    driver.setFirstName(Objects.requireNonNullElse(updatedDriver.getFirstName(),
                                                   driver.getFirstName()));
    driver.setLastName(Objects.requireNonNullElse(updatedDriver.getLastName(),
                                                   driver.getLastName()));
    driver.setLicenseCategory(Objects.requireNonNullElse(updatedDriver.getLicenseCategory(),
                                                   driver.getLicenseCategory()));
    driver.setDateOfBirth(Objects.requireNonNullElse(updatedDriver.getDateOfBirth(),
                                                   driver.getDateOfBirth()));
    driver.setExperience(Objects.requireNonNullElse(updatedDriver.getExperience(),
                                                   driver.getExperience()));
    driverRepository.save(driver);
  }

  @Override
  public Page<DriverGetResponseDto> getAllDrivers(int page,
                                                  int size,
                                                  String sortBy) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
    Page<Driver> driverPage = driverRepository.findAll(pageable);
    return driverPage.map(DriverMapper.INSTANCE::toDtoResponse);
  }

  @Override
  public Page<DriverGetResponseDto> searchDriversByFirstName(String firstName,
                                                             int page,
                                                             int size,
                                                             String sortBy) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
    Page<Driver> driverPage = driverRepository.findByFirstNameContaining(firstName, pageable);
    return driverPage.map(DriverMapper.INSTANCE::toDtoResponse);
  }

  @Override
  public Page<DriverGetResponseDto> searchDriversByLastName(String lastName,
                                                            int page,
                                                            int size,
                                                            String sortBy) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
    Page<Driver> driverPage = driverRepository.findByLastNameContaining(lastName, pageable);
    return driverPage.map(DriverMapper.INSTANCE::toDtoResponse);
  }

  @Override
  public Page<DriverGetResponseDto> searchDriversByPassport(String passport,
                                                            int page,
                                                            int size,
                                                            String sortBy) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
    Page<Driver> driverPage = driverRepository.findByPassportContaining(passport, pageable);
    return driverPage.map(DriverMapper.INSTANCE::toDtoResponse);
  }

  @Override
  public Page<DriverGetResponseDto> searchDriversByExperience(Integer experience,
                                                              int page,
                                                              int size,
                                                              String sortBy) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
    Page<Driver> driverPage = driverRepository.findByExperience(experience, pageable);
    return driverPage.map(DriverMapper.INSTANCE::toDtoResponse);
  }

  @Override
  public String sendCarPurchaseEvent(CarPurchaseEvent carPurchaseEvent)
          throws JsonProcessingException {
    return producer.sendMessageCarPurchase(carPurchaseEvent);
  }

  @Override
  public String sendSuccessfulPaymentEvent(DetailAddEvent detailAddEvent)
          throws JsonProcessingException {
    return producer.sendMessageDetailAdd(detailAddEvent);
  }
}