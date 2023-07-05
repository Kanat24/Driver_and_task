package com.example.carsdetailsmicroservice.dto.car.create;

import com.example.carsdetailsmicroservice.entity.Detail;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Data Transfer Object (DTO) for creating a car.
 * This class represents the request payload for creating a car.
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CarCreateRequestDto {
  @Schema(description = "Car's vin code",
          example = "QWERTY123")
  @NotBlank(message = "{validate.notblank}")
  @Size(min = 10, max = 15, message = "{validate.car.vin.size}")
  @Pattern(regexp = "^[A-Z0-9]+$", message = "{validate.car.vin.pattern}")
  private String vin;
  @Schema(description = "Car's licensePlate",
          example = "QWERTY1")
  @NotBlank(message = "{validate.notblank}")
  @Size(min = 1, max = 7, message = "{validate.car.plate.size}")
  private String licensePlate;
  @Schema(description = "Car's manufacturer",
          example = "Mazda")
  @Nullable
  private String manufacturer;
  @Schema(description = "Car's model",
          example = "Mazda")
  @Nullable
  private String model;
  @Schema(description = "Car's year of manufacturer",
          example = "1998")
  @Nullable
  private Integer year;
  @Schema(description = "List of details",
          example = "QWERTY12345, QWERTY54321")
  @NotEmpty(message = "{validate.car.details.notempty}")
  private Set<Detail> details = new HashSet<>();
}