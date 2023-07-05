package com.example.drivesbillsmicroservice.service;

import com.example.drivesbillsmicroservice.dto.driver.create.DriverCreateRequestDto;
import com.example.drivesbillsmicroservice.dto.driver.get.DriverGetResponseDto;
import com.example.drivesbillsmicroservice.dto.driver.update.DriverUpdateRequestDto;
import com.example.drivesbillsmicroservice.events.CarPurchaseEvent;
import com.example.drivesbillsmicroservice.events.DetailAddEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * Service interface for managing drivers and performing driver-related operations.
 */
@Service
public interface DriverService {

  /**
     * Congratulates the drivers who have their birthday today.
     */
  void congratulateDriversWithBirthday();

  /**
     * Registers a new driver based on the provided driver request DTO.
     *
     * @param carRequestDto the driver request DTO
     */
  void registerDriver(DriverCreateRequestDto carRequestDto);

  /**
     * Searches for drivers by first name and returns a paginated result.
     *
     * @param firstName the first name to search for
     * @param page the page number
     * @param size the page size
     * @param sortBy the field to sort by
     * @return a page of driver response DTOs matching the search criteria
     */
  Page<DriverGetResponseDto> searchDriversByFirstName(String firstName,
                                                      int page,
                                                      int size,
                                                      String sortBy);

  /**
     * Searches for drivers by experience and returns a paginated result.
     *
     * @param experience the experience to search for
     * @param page the page number
     * @param size the page size
     * @param sortBy the field to sort by
     * @return a page of driver response DTOs matching the search criteria
     */
  Page<DriverGetResponseDto> searchDriversByExperience(Integer experience,
                                                       int page,
                                                       int size,
                                                       String sortBy);

  /**
     * Searches for drivers by passport and returns a paginated result.
     *
     * @param passport the passport to search for
     * @param page the page number
     * @param size the page size
     * @param sortBy the field to sort by
     * @return a page of driver response DTOs matching the search criteria
     */
  Page<DriverGetResponseDto> searchDriversByPassport(String passport,
                                                     int page,
                                                     int size,
                                                     String sortBy);

  /**
     * Searches for drivers by last name and returns a paginated result.
     *
     * @param lastName the last name to search for
     * @param page the page number
     * @param size the page size
     * @param sortBy the field to sort by
     * @return a page of driver response DTOs matching the search criteria
     */
  Page<DriverGetResponseDto> searchDriversByLastName(String lastName,
                                                     int page,
                                                     int size,
                                                     String sortBy);

  /**
     * Retrieves all drivers in a paginated result.
     *
     * @param page the page number
     * @param size the page size
     * @param sortBy the field to sort by
     * @return a page of driver response DTOs
     */
  Page<DriverGetResponseDto> getAllDrivers(int page, int size, String sortBy);

  /**
     * Finds a driver by passport number.
     *
     * @param passport the passport number to search for
     * @return an optional containing the found driver response DTO, or empty if not found
     */
  Optional<DriverGetResponseDto> findDriverByPassport(String passport);

  /**
     * Deletes a driver with the specified passport number.
     *
     * @param passport the passport number of the driver to delete
     */
  void deleteDriver(String passport);

  /**
     * Updates the information of a driver with the specified passport number.
     *
     * @param passport the passport number of the driver to update
     * @param updatedCar the updated driver request DTO
     */
  void updateDriver(String passport, DriverUpdateRequestDto updatedCar);

  /**
     * Sends a car purchase event.
     *
     * @param carPurchaseEvent the car purchase event to send
     * @return a string indicating the status of the message sending
     * @throws JsonProcessingException if an error occurs while processing the car purchase event
     */

  String sendCarPurchaseEvent(CarPurchaseEvent carPurchaseEvent) throws JsonProcessingException;

  /**
     * Sends a successful payment event.
     *
     * @param detailAddEvent the detail add event to send
     * @return a string indicating the status of the message sending
     * @throws JsonProcessingException if an error occurs while processing the detail add event
     */
  String sendSuccessfulPaymentEvent(DetailAddEvent detailAddEvent) throws JsonProcessingException;
}
