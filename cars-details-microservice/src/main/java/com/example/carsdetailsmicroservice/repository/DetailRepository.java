package com.example.carsdetailsmicroservice.repository;

import com.example.carsdetailsmicroservice.entity.Detail;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This interface represents a repository for Detail entities.
 */
@Repository
public interface DetailRepository extends JpaRepository<Detail, Long> {
  /**
   * Retrieves a Detail entity by its serial number.
   *
   * @param serialNumber The serial number of the Detail.
   * @return An Optional containing the Detail entity if found, or an empty Optional if not found.
   */
  Optional<Detail> findBySerialNumber(String serialNumber);

  /**
   * Deletes a Detail entity by its serial number.
   *
   * @param serialNumber The serial number of the Detail to delete.
   */
  void deleteBySerialNumber(String serialNumber);

  /**
   * Checks if a Detail entity exists with the specified serial number.
   *
   * @param serialNumber The serial number to check.
   * @return true if a Detail entity with the specified serial number exists, false otherwise.
   */
  boolean existsBySerialNumber(String serialNumber);

  /**
   * Retrieves all Detail entities with pagination.
   *
   * @param pageable The pagination information.
   * @return A Page containing the Detail entities.
   */
  Page<Detail> findAll(Pageable pageable);

  /**
   * Retrieves Detail entities with pagination where the serial number contains the specified value.
   *
   * @param serialNumber The value to search for in the serial number field.
   * @param pageable     The pagination information.
   * @return A Page containing the matching Detail entities.
   */
  Page<Detail> findBySerialNumberContaining(String serialNumber, Pageable pageable);

  /**
   * Retrieves Detail entities with pagination where the price matches the specified value.
   *
   * @param price    The value to match in the price field.
   * @param pageable The pagination information.
   * @return A Page containing the matching Detail entities.
   */
  Page<Detail> findByPrice(BigDecimal price, Pageable pageable);
}