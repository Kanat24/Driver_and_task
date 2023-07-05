package com.example.carsdetailsmicroservice.service;

import com.example.carsdetailsmicroservice.dto.detail.get.DetailGetResponseDto;
import com.example.carsdetailsmicroservice.dto.detail.update.DetailUpdateRequestDto;
import com.example.carsdetailsmicroservice.entity.Detail;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * This interface represents a service for managing Detail entities.
 */
@Service
public interface DetailService {
  /**
   * Creates a new Detail.
   *
   * @param detail The Detail object to create.
   */
  void createDetail(Detail detail);

  /**
   * Retrieves a Detail by its serial number.
   *
   * @param serialNumber The serial number of the Detail to retrieve.
   * @return An Optional containing the DetailGetResponseDto if found, or an empty Optional
   * if not found.
   */
  Optional<DetailGetResponseDto> findDetailBySerialNumber(String serialNumber);

  /**
   * Deletes a Detail by its serial number.
   *
   * @param serialNumber The serial number of the Detail to delete.
   */
  void deleteDetail(String serialNumber);

  /**
   * Updates a Detail with new information.
   *
   * @param serialNumber          The serial number of the Detail to update.
   * @param detailUpdateRequestDto The DetailUpdateRequestDto containing the updated information.
   * @return The updated Detail object.
   */
  Detail changeDetail(String serialNumber,
                      DetailUpdateRequestDto detailUpdateRequestDto);

  /**
   * Retrieves all Details with pagination.
   *
   * @param page   The page number.
   * @param size   The number of items per page.
   * @param sortBy The field to sort the results by.
   * @return A Page containing the DetailGetResponseDto objects.
   */
  Page<DetailGetResponseDto> getAllDetails(int page,
                                           int size,
                                           String sortBy);

  /**
   * Searches for Details by price with pagination.
   *
   * @param price  The price to search for.
   * @param page   The page number.
   * @param size   The number of items per page.
   * @param sortBy The field to sort the results by.
   * @return A Page containing the DetailGetResponseDto objects.
   */
  Page<DetailGetResponseDto> searchDetailsByPrice(BigDecimal price,
                                                  int page,
                                                  int size,
                                                  String sortBy);

  /**
   * Searches for Details by serial number with pagination.
   *
   * @param serialNumber The serial number to search for.
   * @param page         The page number.
   * @param size         The number of items per page.
   * @param sortBy       The field to sort the results by.
   * @return A Page containing the DetailGetResponseDto objects.
   */
  Page<DetailGetResponseDto> searchDetailsBySerialNumber(String serialNumber,
                                                         int page,
                                                         int size,
                                                         String sortBy);
}