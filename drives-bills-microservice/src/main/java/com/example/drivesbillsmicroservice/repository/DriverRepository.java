package com.example.drivesbillsmicroservice.repository;

import com.example.drivesbillsmicroservice.entity.Driver;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing and managing Driver entities in the database.
 */
@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

  /**
     * Retrieves a list of drivers who have their birthday today.
     *
     * @return the list of drivers with a birthday today
     */
  @Query("SELECT d FROM Driver d WHERE DAY(d.dateOfBirth) = "
          + "DAY(CURRENT_DATE) AND MONTH(d.dateOfBirth) = MONTH(CURRENT_DATE)")
  List<Driver> findDriversWithBirthdayToday();

  /**
     * Checks if a driver with the specified passport exists in the database.
     *
     * @param passport the passport number to check
     * @return true if a driver with the passport exists, false otherwise
     */
  boolean existsByPassport(String passport);

  /**
     * Finds a driver by the specified passport number.
     *
     * @param passport the passport number to search for
     * @return an optional containing the found driver, or empty if not found
     */
  Optional<Driver> findDriverByPassport(String passport);

  /**
     * Deletes a driver with the specified passport number.
     *
     * @param passport the passport number of the driver to delete
     */
  void deleteByPassport(String passport);

  /**
     * Retrieves a page of drivers whose first name contains the specified value.
     *
     * @param firstName the value to search for in the first name
     * @param pageable  the pageable information for pagination
     * @return a page of drivers matching the search criteria
     */
  Page<Driver> findByFirstNameContaining(String firstName, Pageable pageable);

  /**
     * Retrieves a page of drivers whose last name contains the specified value.
     *
     * @param lastName  the value to search for in the last name
     * @param pageable  the pageable information for pagination
     * @return a page of drivers matching the search criteria
     */
  Page<Driver> findByLastNameContaining(String lastName, Pageable pageable);

  /**
     * Retrieves a page of drivers whose passport contains the specified value.
     *
     * @param passport  the value to search for in the passport
     * @param pageable  the pageable information for pagination
     * @return a page of drivers matching the search criteria
     */
  Page<Driver> findByPassportContaining(String passport, Pageable pageable);

  /**
     * Retrieves a page of drivers with the specified experience.
     *
     * @param experience the value to search for in the experience
     * @param pageable   the pageable information for pagination
     * @return a page of drivers matching the search criteria
     */
  Page<Driver> findByExperience(Integer experience, Pageable pageable);
}