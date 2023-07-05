package com.example.drivesbillsmicroservice.dto.driver.update;

import com.example.drivesbillsmicroservice.enums.LicenseCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Data transfer object representing the request for updating a driver.
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DriverUpdateRequestDto {
  @Size(min = 3, max = 15, message = "First name should be between 3 and 15 characters")
  private String firstName;
  @Size(min = 3, max = 15, message = "Last name should be between 3 and 15 characters")
  private String lastName;
  @Pattern(regexp = "^[A-Z0-9]+$", message = "Invalid passport format")
  @Size(min = 7, max = 10, message = "Passport should be between 7 and 10 characters")
  private String passport;
  private LicenseCategory licenseCategory;
  @Past(message = "Date of birth should be in the past")
  private LocalDate dateOfBirth;
  @Min(value = 0, message = "Experience cannot be negative")
  private int experience;
}