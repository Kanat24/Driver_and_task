package com.example.carsdetailsmicroservice.dto.car.get;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Data Transfer Object (DTO) for retrieving car details.
 * This class represents the response payload for retrieving car information.
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CarGetResponseDto {
  @Schema(description = "Car's vin code",
          example = "QWERTY12345")
  private String vin;
  @Schema(description = "Car's licensePlate",
          example = "QWERTY1")
  private String licensePlate;
  @Schema(description = "Car's manufacturer",
          example = "Mazda")
  private String manufacturer;
  @Schema(description = "Car's model",
          example = "Mazda")
  private String model;
  @Schema(description = "Car's year of manufacturer",
          example = "1998")
  private Integer year;
  @Schema(description = "Car's driver id",
          example = "1")
  private Long driverId;
}