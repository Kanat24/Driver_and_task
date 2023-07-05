package com.example.carsdetailsmicroservice.dto.car.update;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Data Transfer Object (DTO) for updating a car.
 * This class represents the request payload for updating a car.
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CarUpdateRequestDto {
  @Schema(description = "Car's vin code",
          example = "QWERTY12345")
  @Size(min = 10, max = 15, message = "{validate.car.vin.size}")
  @Pattern(regexp = "^[A-Z0-9]+$", message = "{validate.car.vin.pattern}")
  private String vin;
  @Schema(description = "Car's licensePlate",
          example = "QWE12")
  @Size(min = 1, max = 7, message = "{validate.car.plate.size}")
  private String licensePlate;
  @Schema(description = "Car's manufacturer",
          example = "Mazda")
  @Nullable
  private String manufacturer;
  @Schema(description = "Car's model",
          example = "CX7")
  @Nullable
  private String model;
  @Schema(description = "Car's year of manufacturer",
          example = "1998")
  @Nullable
  private Integer year;
}