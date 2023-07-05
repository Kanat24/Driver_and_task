package com.example.carsdetailsmicroservice.dto.detail.update;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Data Transfer Object (DTO) for updating details.
 * This class represents the request payload for updating details.
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DetailUpdateRequestDto {
  @Schema(description = "Detail's serial number",
          example = "QWERTY12345")
  @NotBlank(message = "{validate.notblank}")
  @Size(min = 3, max = 15, message = "{validate.detail.number.size}")
  @Pattern(regexp = "^[A-Z0-9]+$", message = "{validate.detail.number.pattern}")
  private String serialNumber;
  @Schema(description = "Price of detail",
          example = "20")
  private BigDecimal price;
}