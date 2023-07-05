package com.example.drivesbillsmicroservice.dto.driver.get;

import com.example.drivesbillsmicroservice.enums.LicenseCategory;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Data transfer object representing the response for getting a driver.
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DriverGetResponseDto {
  private String firstName;
  private String lastName;
  private String passport;
  private LicenseCategory licenseCategory;
  private LocalDate dateOfBirth;
  private int experience;
}