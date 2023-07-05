package com.example.drivesbillsmicroservice.service.implementation;

import com.example.drivesbillsmicroservice.dto.driver.create.DriverCreateRequestDto;
import com.example.drivesbillsmicroservice.dto.driver.get.DriverGetResponseDto;
import com.example.drivesbillsmicroservice.dto.driver.update.DriverUpdateRequestDto;
import com.example.drivesbillsmicroservice.entity.Driver;
import com.example.drivesbillsmicroservice.enums.LicenseCategory;
import com.example.drivesbillsmicroservice.events.CarPurchaseEvent;
import com.example.drivesbillsmicroservice.events.DetailAddEvent;
import com.example.drivesbillsmicroservice.exceptions.driver.DriverAlreadyExistException;
import com.example.drivesbillsmicroservice.exceptions.driver.DriverNotFoundException;
import com.example.drivesbillsmicroservice.kafka.Producer;
import com.example.drivesbillsmicroservice.repository.DriverRepository;
import com.example.drivesbillsmicroservice.utils.TestConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageImpl;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DriverServiceImplementationTest {
  @Mock
  private DriverRepository driverRepository;

  @Mock
  private Producer producer;

  @InjectMocks
  private DriverServiceImplementation driverService;

  @Test
  void registerDriver_CreatesDriver_WhenDriverDoesNotExist() {
    DriverCreateRequestDto driverCreateRequestDto = createDriverCreateRequestDto();
    when(driverRepository.existsByPassport(driverCreateRequestDto.getPassport())).thenReturn(false);
    driverService.registerDriver(driverCreateRequestDto);
    verify(driverRepository).save(any(Driver.class));
  }

  @Test
  void registerDriver_ThrowsDriverAlreadyExistException_WhenDriverAlreadyExists() {
    DriverCreateRequestDto driverCreateRequestDto = createDriverCreateRequestDto();
    when(driverRepository.existsByPassport(driverCreateRequestDto.getPassport())).thenReturn(true);
    assertThrows(DriverAlreadyExistException.class, () -> driverService.registerDriver(driverCreateRequestDto));
  }

  @Test
  void findDriverByPassport_ReturnsDriver_WhenDriverExists() {
    Driver driver = createDriver(TestConstants.FIRST_NAME, TestConstants.LAST_NAME, TestConstants.PASSPORT, LocalDate.now());
    when(driverRepository.findDriverByPassport(TestConstants.PASSPORT)).thenReturn(Optional.of(driver));
    Optional<DriverGetResponseDto> result = driverService.findDriverByPassport(TestConstants.PASSPORT);
    assertTrue(result.isPresent());
    assertEquals(driver.getFirstName(), result.get().getFirstName());
    assertEquals(driver.getLastName(), result.get().getLastName());
  }

  @Test
  void findDriverByPassport_ThrowsDriverNotFoundException_WhenDriverDoesNotExist() {
    when(driverRepository.findDriverByPassport(TestConstants.PASSPORT)).thenReturn(Optional.empty());
    assertThrows(DriverNotFoundException.class, () -> driverService.findDriverByPassport(TestConstants.PASSPORT));
  }

  @Test
  void deleteDriver_DeletesDriver_WhenDriverExists() {
    Optional<Driver> driver = Optional.of(createDriver(TestConstants.FIRST_NAME, TestConstants.LAST_NAME, TestConstants.PASSPORT, LocalDate.now()));
    when(driverRepository.findDriverByPassport(TestConstants.PASSPORT)).thenReturn(driver);
    driverService.deleteDriver(TestConstants.PASSPORT);
    verify(driverRepository).deleteByPassport(TestConstants.PASSPORT);
  }

  @Test
  void deleteDriver_ThrowsDriverNotFoundException_WhenDriverDoesNotExist() {
    when(driverRepository.findDriverByPassport(TestConstants.PASSPORT)).thenReturn(Optional.empty());
    assertThrows(DriverNotFoundException.class, () -> driverService.deleteDriver(TestConstants.PASSPORT));
  }

  @Test
  void updateDriver_UpdatesDriver_WhenDriverExists() {
    DriverUpdateRequestDto updatedDriver = createUpdatedDriver();
    Driver existingDriver = createDriver(TestConstants.FIRST_NAME, TestConstants.LAST_NAME, TestConstants.PASSPORT, LocalDate.now());
    when(driverRepository.findDriverByPassport(TestConstants.PASSPORT)).thenReturn(Optional.of(existingDriver));
    driverService.updateDriver(TestConstants.PASSPORT, updatedDriver);
    verify(driverRepository).save(existingDriver);
    assertEquals(updatedDriver.getFirstName(), existingDriver.getFirstName());
    assertEquals(updatedDriver.getLastName(), existingDriver.getLastName());
  }

  @Test
  void updateDriver_ThrowsDriverNotFoundException_WhenDriverDoesNotExist() {
    DriverUpdateRequestDto updatedDriver = createUpdatedDriver();
    when(driverRepository.findDriverByPassport(TestConstants.PASSPORT)).thenReturn(Optional.empty());
    assertThrows(DriverNotFoundException.class, () -> driverService.updateDriver(TestConstants.PASSPORT, updatedDriver));
  }

  @Test
  void getAllDrivers_ReturnsAllDrivers() {
    Pageable pageable = PageRequest.of(TestConstants.PAGE, TestConstants.SIZE, Sort.by(TestConstants.SORT_BY_LAST_NAME));
    Page<Driver> driverPage = createDriverPage(pageable);
    when(driverRepository.findAll(pageable)).thenReturn(driverPage);
    Page<DriverGetResponseDto> result = driverService.getAllDrivers(TestConstants.PAGE, TestConstants.SIZE, TestConstants.SORT_BY_LAST_NAME);
    assertEquals(driverPage.getTotalElements(), result.getTotalElements());
    assertEquals(driverPage.getTotalPages(), result.getTotalPages());
  }

  @Test
  void searchDriversByFirstName_ReturnsMatchingDrivers() {
    Pageable pageable = PageRequest.of(TestConstants.PAGE, TestConstants.SIZE, Sort.by(TestConstants.SORT_BY_LAST_NAME));
    Page<Driver> driverPage = createDriverPage(pageable);
    when(driverRepository.findByFirstNameContaining(TestConstants.FIRST_NAME, pageable)).thenReturn(driverPage);
    Page<DriverGetResponseDto> result = driverService.searchDriversByFirstName(TestConstants.FIRST_NAME, TestConstants.PAGE, TestConstants.SIZE, TestConstants.SORT_BY_LAST_NAME);
    assertEquals(driverPage.getTotalElements(), result.getTotalElements());
    assertEquals(driverPage.getTotalPages(), result.getTotalPages());
  }

  @Test
  void searchDriversByLastName_ReturnsMatchingDrivers() {
    Pageable pageable = PageRequest.of(TestConstants.PAGE, TestConstants.SIZE, Sort.by(TestConstants.SORT_BY_FIRST_NAME));
    Page<Driver> driverPage = createDriverPage(pageable);
    when(driverRepository.findByLastNameContaining(TestConstants.LAST_NAME, pageable)).thenReturn(driverPage);
    Page<DriverGetResponseDto> result = driverService.searchDriversByLastName(TestConstants.LAST_NAME, TestConstants.PAGE, TestConstants.SIZE, TestConstants.SORT_BY_FIRST_NAME);
    assertEquals(driverPage.getTotalElements(), result.getTotalElements());
    assertEquals(driverPage.getTotalPages(), result.getTotalPages());
  }

  @Test
  void searchDriversByPassport_ReturnsMatchingDrivers() {
    Pageable pageable = PageRequest.of(TestConstants.PAGE, TestConstants.SIZE, Sort.by(TestConstants.SORT_BY_LAST_NAME));
    Page<Driver> driverPage = createDriverPage(pageable);
    when(driverRepository.findByPassportContaining(TestConstants.PASSPORT, pageable)).thenReturn(driverPage);
    Page<DriverGetResponseDto> result = driverService.searchDriversByPassport(TestConstants.PASSPORT, TestConstants.PAGE, TestConstants.SIZE, TestConstants.SORT_BY_LAST_NAME);
    assertEquals(driverPage.getTotalElements(), result.getTotalElements());
    assertEquals(driverPage.getTotalPages(), result.getTotalPages());
  }

  @Test
  void searchDriversByExperience_ReturnsMatchingDrivers() {
    Pageable pageable = PageRequest.of(TestConstants.PAGE, TestConstants.SIZE, Sort.by(TestConstants.SORT_BY_LAST_NAME));
    Page<Driver> driverPage = createDriverPage(pageable);
    when(driverRepository.findByExperience(TestConstants.EXPERIENCE, pageable)).thenReturn(driverPage);
    Page<DriverGetResponseDto> result = driverService.searchDriversByExperience(TestConstants.EXPERIENCE, TestConstants.PAGE, TestConstants.SIZE, TestConstants.SORT_BY_LAST_NAME);
    assertEquals(driverPage.getTotalElements(), result.getTotalElements());
    assertEquals(driverPage.getTotalPages(), result.getTotalPages());
  }

  @Test
  void sendCarPurchaseEvent_ReturnsMessageSentByProducer() throws JsonProcessingException {
    CarPurchaseEvent carPurchaseEvent = createCarPurchaseEvent();
    when(producer.sendMessageCarPurchase(carPurchaseEvent)).thenReturn(TestConstants.MESSAGE_SENT);
    String result = driverService.sendCarPurchaseEvent(carPurchaseEvent);
    assertEquals(TestConstants.MESSAGE_SENT, result);
  }

  @Test
  void sendSuccessfulPaymentEvent_ReturnsMessageSentByProducer() throws JsonProcessingException {
    DetailAddEvent detailAddEvent = createDetailAddEvent();
    when(producer.sendMessageDetailAdd(detailAddEvent)).thenReturn(TestConstants.MESSAGE_SENT);
    String result = driverService.sendSuccessfulPaymentEvent(detailAddEvent);
    assertEquals(TestConstants.MESSAGE_SENT, result);
  }

  private DriverCreateRequestDto createDriverCreateRequestDto() {
    DriverCreateRequestDto dto = new DriverCreateRequestDto();
    dto.setFirstName(TestConstants.FIRST_NAME);
    dto.setLastName(TestConstants.LAST_NAME);
    dto.setPassport(TestConstants.PASSPORT);
    dto.setDateOfBirth(LocalDate.of(1990, 5, 10));
    return dto;
  }

  private Driver createDriver(String firstName, String lastName, String passport, LocalDate dateOfBirth) {
    Driver driver = new Driver();
    driver.setFirstName(firstName);
    driver.setLastName(lastName);
    driver.setPassport(passport);
    driver.setDateOfBirth(dateOfBirth);
    return driver;
  }

  private DriverUpdateRequestDto createUpdatedDriver() {
    DriverUpdateRequestDto dto = new DriverUpdateRequestDto();
    dto.setFirstName(TestConstants.FIRST_NAME);
    dto.setLastName(TestConstants.LAST_NAME);
    dto.setLicenseCategory(LicenseCategory.B);
    return dto;
  }

  private Page<Driver> createDriverPage(Pageable pageable) {
    List<Driver> drivers = new ArrayList<>();
    return new PageImpl<>(drivers, pageable, drivers.size());
  }

  private CarPurchaseEvent createCarPurchaseEvent() {
    CarPurchaseEvent event = new CarPurchaseEvent();
    event.setLicensePlate(TestConstants.LICENSE_PLATE);
    event.setDriverId(1L);
    return event;
  }

  private DetailAddEvent createDetailAddEvent() {
    DetailAddEvent event = new DetailAddEvent();
    event.setSerialNumber(TestConstants.SERIAL_NUMBER);
    event.setLicensePlate(TestConstants.LICENSE_PLATE);
    return event;
  }
}