package com.example.carsdetailsmicroservice.service;

import com.example.carsdetailsmicroservice.dto.car.create.CarCreateRequestDto;
import com.example.carsdetailsmicroservice.dto.car.get.CarGetResponseDto;
import com.example.carsdetailsmicroservice.dto.car.update.CarUpdateRequestDto;
import com.example.carsdetailsmicroservice.entity.Car;
import com.example.carsdetailsmicroservice.events.CarPurchaseEvent;
import com.example.carsdetailsmicroservice.events.DetailAddEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * This interface represents a service for managing Car entities.
 */
@Service
public interface CarService {
  /**
   * Registers a new Car.
   *
   * @param carRequestDto The CarCreateRequestDto containing the details of the Car to register.
   */
  void registerCar(CarCreateRequestDto carRequestDto);

  /**
   * Retrieves a Car by its VIN.
   *
   * @param vin The VIN of the Car to retrieve.
   * @return An Optional containing the CarGetResponseDto if found, or an empty Optional
   * if not found.
   */
  Optional<CarGetResponseDto> findCarByVin(String vin);

  /**
   * Retrieves a Car by its license plate.
   *
   * @param licensePlate The license plate of the Car to retrieve.
   * @return An Optional containing the CarGetResponseDto if found, or an empty Optional
   * if not found.
   */
  Optional<CarGetResponseDto> findCarByLicensePlate(String licensePlate);

  /**
   * Deletes a Car by its VIN.
   *
   * @param vin The VIN of the Car to delete.
   */
  void deleteCar(String vin);

  /**
   * Updates a Car with new information.
   *
   * @param vin          The VIN of the Car to update.
   * @param updatedCar   The CarUpdateRequestDto containing the updated information.
   * @return The updated Car object.
   */
  Car updateCar(String vin, CarUpdateRequestDto updatedCar);

  /**
   * Retrieves all Cars with pagination.
   *
   * @param page   The page number.
   * @param size   The number of items per page.
   * @param sortBy The field to sort the results by.
   * @return A Page containing the CarGetResponseDto objects.
   */
  Page<CarGetResponseDto> getAllCars(int page, int size, String sortBy);

  /**
   * Searches for Cars by manufacturer with pagination.
   *
   * @param manufacturer The manufacturer to search for.
   * @param page         The page number.
   * @param size         The number of items per page.
   * @param sortBy       The field to sort the results by.
   * @return A Page containing the CarGetResponseDto objects.
   */
  Page<CarGetResponseDto> searchCarsByManufacturer(String manufacturer,
                                                   int page,
                                                   int size,
                                                   String sortBy);

  /**
   * Searches for Cars by model with pagination.
   *
   * @param model The model to search for.
   * @param page  The page number.
   * @param size  The number of items per page.
   * @param sortBy The field to sort the results by.
   * @return A Page containing the CarGetResponseDto objects.
   */
  Page<CarGetResponseDto> searchCarsByModel(String model, int page, int size, String sortBy);

  /**
   * Searches for Cars by year with pagination.
   *
   * @param year  The year to search for.
   * @param page  The page number.
   * @param size  The number of items per page.
   * @param sortBy The field to sort the results by.
   * @return A Page containing the CarGetResponseDto objects.
   */
  Page<CarGetResponseDto> searchCarsByYear(Integer year, int page, int size, String sortBy);

  /**
   * Processes a CarEventDto for buying a car.
   *
   * @param carPurchaseEvent The CarEventDto representing the car purchase event.
   */
  void byuCar(CarPurchaseEvent carPurchaseEvent);

  /**
   * Adds a detail to a car.
   *
   * @param detailAddEvent The DetailAddEvent containing the details of the detail to add.
   * @return A message indicating the success of the operation.
   * @throws JsonProcessingException if there is an error while processing the JSON.
   */
  String addDetail(DetailAddEvent detailAddEvent) throws JsonProcessingException;

  /**
   * Updates the details of a car.
   *
   * @param licensePlate The license plate of the car to update.
   * @param serialNumber The serial number of the detail to update.
   * @return The updated Car object.
   */
  Car updateCarDetail(String licensePlate, String serialNumber);
}