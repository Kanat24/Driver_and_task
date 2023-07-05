package com.example.carsdetailsmicroservice.service.implementation;

import com.example.carsdetailsmicroservice.dto.car.create.CarCreateRequestDto;
import com.example.carsdetailsmicroservice.dto.car.get.CarGetResponseDto;
import com.example.carsdetailsmicroservice.dto.car.update.CarUpdateRequestDto;
import com.example.carsdetailsmicroservice.dto.detail.get.DetailGetResponseDto;
import com.example.carsdetailsmicroservice.entity.Car;
import com.example.carsdetailsmicroservice.entity.Detail;
import com.example.carsdetailsmicroservice.events.CarPurchaseEvent;
import com.example.carsdetailsmicroservice.events.DetailAddEvent;
import com.example.carsdetailsmicroservice.exceptions.ErrorCode;
import com.example.carsdetailsmicroservice.exceptions.car.CarAlreadyExistException;
import com.example.carsdetailsmicroservice.exceptions.car.CarNotFoundException;
import com.example.carsdetailsmicroservice.exceptions.detail.DetailNotFoundException;
import com.example.carsdetailsmicroservice.kafka.Producer;
import com.example.carsdetailsmicroservice.mapper.CarMapper;
import com.example.carsdetailsmicroservice.repository.CarRepository;
import com.example.carsdetailsmicroservice.repository.DetailRepository;
import com.example.carsdetailsmicroservice.service.CarService;
import com.example.carsdetailsmicroservice.service.DetailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CarServiceImplementation implements CarService {
  private final CarRepository carRepository;
  private final EntityManager entityManager;
  private final DetailService detailService;
  private final DetailRepository detailRepository;
  private final Producer producer;

  @Autowired
  public CarServiceImplementation(CarRepository carRepository,
                                    EntityManager entityManager,
                                    DetailService detailService,
                                    DetailRepository detailRepository,
                                    Producer producer) {
    this.carRepository = carRepository;
    this.entityManager = entityManager;
    this.detailService = detailService;
    this.detailRepository = detailRepository;
    this.producer = producer;
  }

  @Override
  public void registerCar(CarCreateRequestDto carRequestDto) {
    String vin = carRequestDto.getVin();
    String licensePlate = carRequestDto.getLicensePlate();
    if (carRepository.existsByVin(vin) || carRepository.existsByLicensePlate(licensePlate)) {
      throw new CarAlreadyExistException(ErrorCode.CAR_ALREADY_EXIST);
    }
    Set<Detail> details = carRequestDto.getDetails();
    details.forEach(detailService::createDetail);
    Car car = CarMapper.INSTANCE.toCar(carRequestDto);
    carRepository.save(car);
  }

  @Override
  public Page<CarGetResponseDto> getAllCars(int page, int size, String sortBy) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
    Page<Car> carPage = carRepository.findAll(pageable);
    return carPage.map(CarMapper.INSTANCE::toDtoResponse);
  }

  @Override
  public Page<CarGetResponseDto> searchCarsByManufacturer(String manufacturer,
                                                            int page,
                                                            int size,
                                                            String sortBy) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
    Page<Car> carPage = carRepository.findByManufacturerContaining(manufacturer, pageable);
    return carPage.map(CarMapper.INSTANCE::toDtoResponse);
  }

  @Override
  public Page<CarGetResponseDto> searchCarsByModel(String model,
                                                     int page,
                                                     int size,
                                                     String sortBy) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
    Page<Car> carPage = carRepository.findByModelContaining(model, pageable);
    return carPage.map(CarMapper.INSTANCE::toDtoResponse);
  }

  @Override
  public Page<CarGetResponseDto> searchCarsByYear(Integer year,
                                                    int page,
                                                    int size,
                                                    String sortBy) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
    Page<Car> carPage = carRepository.findByYear(year, pageable);
    return carPage.map(CarMapper.INSTANCE::toDtoResponse);
  }

  @Override
  public Optional<CarGetResponseDto> findCarByVin(String vin) {
    Optional<Car> car = carRepository.findByVin(vin);
    if (car.isEmpty()) {
      throw new CarNotFoundException(ErrorCode.CAR_NOT_FOUND);
    }
    return car.map(CarMapper.INSTANCE::toDtoResponse);
  }

  @Override
  public Optional<CarGetResponseDto> findCarByLicensePlate(String licensePlate) {
    Optional<Car> car = carRepository.findCarByLicensePlate(licensePlate);
    if (car.isEmpty()) {
      throw new CarNotFoundException(ErrorCode.CAR_NOT_FOUND);
    }
    return car.map(CarMapper.INSTANCE::toDtoResponse);
  }

  @Transactional
  @Override
  public void deleteCar(String vin) {
    if (!carRepository.existsByVin(vin)) {
      throw new CarNotFoundException(ErrorCode.CAR_NOT_FOUND);
    }
    carRepository.deleteByVin(vin);
  }

  @Transactional
  public Car updateCar(String vin, CarUpdateRequestDto updatedCar) {
    if (!carRepository.existsByVin(vin)) {
      throw new CarNotFoundException(ErrorCode.CAR_NOT_FOUND);
    }
    Car car = carRepository.findByVin(vin).orElseThrow(() ->
              new CarNotFoundException(ErrorCode.CAR_NOT_FOUND));
    car.setLicensePlate(Objects.requireNonNullElse(updatedCar.getLicensePlate(),
                car.getLicensePlate()));
    car.setManufacturer(Objects.requireNonNullElse(updatedCar.getManufacturer(),
                car.getManufacturer()));
    car.setModel(Objects.requireNonNullElse(updatedCar.getModel(), car.getModel()));
    car.setYear(Objects.requireNonNullElse(updatedCar.getYear(), car.getYear()));
    return carRepository.save(car);
  }

  @Transactional
  public void byuCar(CarPurchaseEvent carPurchaseEvent) {
    if (!carRepository.existsByLicensePlate(carPurchaseEvent.getLicensePlate())) {
      throw new CarNotFoundException(ErrorCode.CAR_NOT_FOUND);
    }
    Optional<Car> optionalCar = carRepository
        .findCarByLicensePlate(carPurchaseEvent.getLicensePlate());
    if (optionalCar.isPresent()) {
      Car car = optionalCar.get();
      entityManager.detach(car);
      car.setDriverId(carPurchaseEvent.getDriverId());
      entityManager.merge(car);
      log.info("Car order persisted: {}", carPurchaseEvent);
    } else {
      throw new CarNotFoundException(ErrorCode.CAR_NOT_FOUND);
    }
  }

  @Override
  public String addDetail(DetailAddEvent detailAddEvent) throws JsonProcessingException {
    Optional<CarGetResponseDto> car = findCarByLicensePlate(detailAddEvent.getLicensePlate());
    System.err.println(car.get());
    detailAddEvent.setDriverId(car.get().getDriverId());
    Optional<DetailGetResponseDto> detail = detailService
            .findDetailBySerialNumber(detailAddEvent.getSerialNumber());
    detailAddEvent.setPrice(detail.get().getPrice());
    return producer.sendDetailAddEventMessage(detailAddEvent);
  }

  @Transactional
  @Override
  public Car updateCarDetail(String licensePlate, String serialNumber) {
    Car car = carRepository.findCarByLicensePlate(licensePlate).orElseThrow(() ->
      new CarNotFoundException(ErrorCode.CAR_NOT_FOUND));
    Detail detail = detailRepository.findBySerialNumber(serialNumber).orElseThrow(() ->
      new DetailNotFoundException(ErrorCode.DETAIL_NOT_FOUND));
    car.getDetails().add(detail);
    return carRepository.save(car);
  }
}