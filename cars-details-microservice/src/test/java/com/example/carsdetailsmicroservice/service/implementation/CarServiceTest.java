package com.example.carsdetailsmicroservice.service.implementation;

import com.example.carsdetailsmicroservice.dto.car.create.CarCreateRequestDto;
import com.example.carsdetailsmicroservice.dto.car.get.CarGetResponseDto;
import com.example.carsdetailsmicroservice.dto.car.update.CarUpdateRequestDto;
import com.example.carsdetailsmicroservice.entity.Car;
import com.example.carsdetailsmicroservice.entity.Detail;
import com.example.carsdetailsmicroservice.exceptions.car.CarAlreadyExistException;
import com.example.carsdetailsmicroservice.exceptions.car.CarNotFoundException;
import com.example.carsdetailsmicroservice.kafka.Producer;
import com.example.carsdetailsmicroservice.mapper.CarMapper;
import com.example.carsdetailsmicroservice.repository.CarRepository;
import com.example.carsdetailsmicroservice.repository.DetailRepository;
import com.example.carsdetailsmicroservice.service.CarService;
import com.example.carsdetailsmicroservice.service.DetailService;
import com.example.carsdetailsmicroservice.utils.TestConstants;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;

class CarServiceTest {
  @Mock
  private CarRepository carRepository;

  @Mock
  private DetailService detailService;
  @Mock
  private EntityManager entityManager;
  @Mock
  DetailRepository detailRepository;
  @Mock
  private Producer producer;

  private CarService carService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    carService = new CarServiceImplementation(carRepository,
                                              entityManager,
                                              detailService,
                                              detailRepository,
                                              producer);
    }

  @Test
  void registerCar_SuccessfulRegistration() {
    CarCreateRequestDto carRequestDto = createCarRequestDto();
    Car car = CarMapper.INSTANCE.toCar(carRequestDto);
    when(carRepository.existsByVin(carRequestDto.getVin())).thenReturn(false);
    when(carRepository.existsByLicensePlate(carRequestDto.getLicensePlate())).thenReturn(false);
    ArgumentCaptor<Car> carCaptor = ArgumentCaptor.forClass(Car.class);
    carService.registerCar(carRequestDto);
    verify(detailService, times(carRequestDto.getDetails().size())).createDetail(any(Detail.class));
    verify(carRepository, times(1)).save(carCaptor.capture());
    Car capturedCar = carCaptor.getValue();
    assertEquals(car.getVin(), capturedCar.getVin());
    assertEquals(car.getLicensePlate(), capturedCar.getLicensePlate());
  }

  @Test
  void registerCar_CarAlreadyExists_ThrowsCarAlreadyExistException() {
    CarCreateRequestDto carRequestDto = createCarRequestDto();
    when(carRepository.existsByVin(carRequestDto.getVin())).thenReturn(true);
    assertThrows(CarAlreadyExistException.class, () -> carService.registerCar(carRequestDto));
    verify(carRepository, never()).save(any(Car.class));
  }

  private CarCreateRequestDto createCarRequestDto() {
    CarCreateRequestDto carRequestDto = new CarCreateRequestDto();
    carRequestDto.setVin(TestConstants.VIN_1);
    carRequestDto.setLicensePlate(TestConstants.LICENSE_PLATE_1);
    carRequestDto.setDetails(createDetails());
    return carRequestDto;
  }

  private Set<Detail> createDetails() {
    Set<Detail> details = new HashSet<>();
    Detail detail1 = new Detail();
    detail1.setId(1L);
    detail1.setSerialNumber(TestConstants.SERIAL_NUMBER);
    details.add(detail1);
    Detail detail2 = new Detail();
    detail2.setId(2L);
    detail2.setSerialNumber(TestConstants.SERIAL_NUMBER_2);
    details.add(detail2);
    return details;
  }

  @Test
  void getAllCars_ReturnsCarPage() {
    List<Car> cars = new ArrayList<>();
    cars.add(createCar(TestConstants.VIN_1, TestConstants.LICENSE_PLATE_1));
    cars.add(createCar(TestConstants.VIN_2, TestConstants.LICENSE_PLATE_2));
    Pageable pageable = PageRequest.of(TestConstants.PAGE, TestConstants.SIZE, Sort.by(TestConstants.SORT_BY_VIN));
    Page<Car> carPage = new PageImpl<>(cars, pageable, cars.size());
    when(carRepository.findAll(pageable)).thenReturn(carPage);
    Page<CarGetResponseDto> result = carService.getAllCars(TestConstants.PAGE, TestConstants.SIZE, TestConstants.SORT_BY_VIN);
    assertEquals(cars.size(), result.getTotalElements());
    assertEquals(cars.size(), result.getContent().size());
  }

  private Car createCar(String vin, String licensePlate) {
    Car car = new Car();
    car.setVin(vin);
    car.setLicensePlate(licensePlate);
    return car;
  }

  @Test
  void searchCarsByManufacturer_ReturnsMatchingCarsPage() {
    List<Car> matchingCars = new ArrayList<>();
    matchingCars.add(createCar1(TestConstants.VIN_1, TestConstants.LICENSE_PLATE_1, TestConstants.MANUFACTURER));
    matchingCars.add(createCar(TestConstants.VIN_2, TestConstants.LICENSE_PLATE_2, TestConstants.MANUFACTURER));
    Pageable pageable = PageRequest.of(TestConstants.PAGE, TestConstants.SIZE, Sort.by(TestConstants.SORT_BY_VIN));
    Page<Car> matchingCarsPage = new PageImpl<>(matchingCars, pageable, matchingCars.size());
    when(carRepository.findByManufacturerContaining(TestConstants.MANUFACTURER, pageable)).thenReturn(matchingCarsPage);
    Page<CarGetResponseDto> result = carService.searchCarsByManufacturer(TestConstants.MANUFACTURER, TestConstants.PAGE, TestConstants.SIZE, TestConstants.SORT_BY_VIN);
    assertEquals(matchingCars.size(), result.getTotalElements());
    assertEquals(matchingCars.size(), result.getContent().size());
  }

  private Car createCar1(String vin, String licensePlate, String manufacturer) {
    Car car = new Car();
    car.setVin(vin);
    car.setLicensePlate(licensePlate);
    car.setManufacturer(manufacturer);
    return car;
  }

  @Test
  void findCarByLicensePlate_ReturnsCarDto_WhenCarExists() {
    Car car = createCar(TestConstants.VIN_1, TestConstants.LICENSE_PLATE_1,TestConstants.MANUFACTURER);
    Optional<Car> optionalCar = Optional.of(car);
    when(carRepository.findCarByLicensePlate(TestConstants.LICENSE_PLATE_1)).thenReturn(optionalCar);
    Optional<CarGetResponseDto> result = carService.findCarByLicensePlate(TestConstants.LICENSE_PLATE_1);
    assertTrue(result.isPresent());
    assertEquals(car.getVin(), result.get().getVin());
    assertEquals(car.getLicensePlate(), result.get().getLicensePlate());
  }

  @Test
  void findCarByLicensePlate_ThrowsCarNotFoundException_WhenCarDoesNotExist() {
     when(carRepository.findCarByLicensePlate(TestConstants.LICENSE_PLATE_1)).thenReturn(Optional.empty());
     assertThrows(CarNotFoundException.class, () -> carService.findCarByLicensePlate(TestConstants.LICENSE_PLATE_1));
  }

  @Test
  void deleteCar_DeletesCar_WhenCarExists() {
    when(carRepository.existsByVin(TestConstants.VIN_1)).thenReturn(true);
    carService.deleteCar(TestConstants.VIN_1);
    verify(carRepository, times(1)).deleteByVin(TestConstants.VIN_1);
  }

  @Test
  void deleteCar_ThrowsCarNotFoundException_WhenCarDoesNotExist() {
    when(carRepository.existsByVin(TestConstants.VIN_1)).thenReturn(false);
    assertThrows(CarNotFoundException.class, () -> carService.deleteCar(TestConstants.VIN_1));
  }

  @Test
  void updateCar_UpdatesCar_WhenCarExists() {
    CarUpdateRequestDto updatedCar = createUpdatedCar(TestConstants.LICENSE_PLATE_1, TestConstants.MANUFACTURER, TestConstants.MODEL, 2022);
    Car existingCar = createCar(TestConstants.VIN_1, TestConstants.LICENSE_PLATE_2, TestConstants.MANUFACTURER);
    when(carRepository.existsByVin(TestConstants.VIN_1)).thenReturn(true);
    when(carRepository.findByVin(TestConstants.VIN_1)).thenReturn(Optional.of(existingCar));
    when(carRepository.save(any(Car.class))).thenAnswer(invocation -> invocation.getArgument(0));
    Car result = carService.updateCar(TestConstants.VIN_1, updatedCar);
    assertEquals(updatedCar.getLicensePlate(), result.getLicensePlate());
    assertEquals(updatedCar.getManufacturer(), result.getManufacturer());
    assertEquals(updatedCar.getModel(), result.getModel());
    assertEquals(updatedCar.getYear(), result.getYear());
  }

  @Test
  void updateCar_ThrowsCarNotFoundException_WhenCarDoesNotExist() {
    CarUpdateRequestDto updatedCar = createUpdatedCar(TestConstants.LICENSE_PLATE_1, TestConstants.MANUFACTURER, TestConstants.MODEL, 2022);
    when(carRepository.existsByVin(TestConstants.VIN_1)).thenReturn(false);
    assertThrows(CarNotFoundException.class, () -> carService.updateCar(TestConstants.VIN_1, updatedCar));
  }

  private Car createCar(String vin, String licensePlate, String manufacturer) {
    Car car = new Car();
    car.setVin(vin);
    car.setLicensePlate(licensePlate);
    car.setManufacturer(manufacturer);
    return car;
  }

  private CarUpdateRequestDto createUpdatedCar(String licensePlate,
                                               String manufacturer,
                                               String model,
                                               int year) {
    CarUpdateRequestDto updatedCar = new CarUpdateRequestDto();
    updatedCar.setLicensePlate(licensePlate);
    updatedCar.setManufacturer(manufacturer);
    updatedCar.setModel(model);
    updatedCar.setYear(year);
    return updatedCar;
  }
}