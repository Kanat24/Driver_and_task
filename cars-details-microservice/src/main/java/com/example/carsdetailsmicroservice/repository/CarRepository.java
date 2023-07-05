package com.example.carsdetailsmicroservice.repository;

import com.example.carsdetailsmicroservice.entity.Car;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This interface represents a repository for Car entities.
 */
@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
  /**
   * Retrieves a Car entity by its VIN.
   *
   * @param vin The VIN of the Car.
   * @return An Optional containing the Car entity if found, or an empty Optional if not found.
   */
  Optional<Car> findByVin(String vin);

  /**
   * Retrieves a Car entity by its license plate.
   *
   * @param licensePlate The license plate of the Car.
   * @return An Optional containing the Car entity if found, or an empty Optional if not found.
   */
  Optional<Car> findCarByLicensePlate(String licensePlate);

  /**
   * Deletes a Car entity by its VIN.
   *
   * @param vin The VIN of the Car to delete.
   */
  void deleteByVin(String vin);

  /**
   * Checks if a Car entity exists with the specified license plate.
   *
   * @param serialNumber The license plate to check.
   * @return true if a Car entity with the specified license plate exists, false otherwise.
   */
  boolean existsByLicensePlate(String serialNumber);

  /**
   * Checks if a Car entity exists with the specified VIN.
   *
   * @param vin The VIN to check.
   * @return true if a Car entity with the specified VIN exists, false otherwise.
   */
  boolean existsByVin(String vin);

  /**
   * Retrieves all Car entities with pagination.
   *
   * @param pageable The pagination information.
   * @return A Page containing the Car entities.
   */
  Page<Car> findAll(Pageable pageable);

  /**
   * Retrieves Car entities with pagination where the manufacturer contains the specified value.
   *
   * @param manufacturer The value to search for in the manufacturer field.
   * @param pageable     The pagination information.
   * @return A Page containing the matching Car entities.
   */
  Page<Car> findByManufacturerContaining(String manufacturer, Pageable pageable);

  /**
   * Retrieves Car entities with pagination where the model contains the specified value.
   *
   * @param model    The value to search for in the model field.
   * @param pageable The pagination information.
   * @return A Page containing the matching Car entities.
   */
  Page<Car> findByModelContaining(String model, Pageable pageable);

  /**
   * Retrieves Car entities with pagination where the year matches the specified value.
   *
   * @param year     The value to match in the year field.
   * @param pageable The pagination information.
   * @return A Page containing the matching Car entities.
   */
  Page<Car> findByYear(Integer year, Pageable pageable);
}