package com.example.carsdetailsmicroservice.dto.detail.get;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Data Transfer Object (DTO) for retrieving details.
 * This class represents the response payload for retrieving details.
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DetailGetResponseDto {
  @Schema(description = "Detail's serial number",
          example = "QWERTY12345")
  private String serialNumber;
  @Schema(description = "Price of detail",
          example = "20")
  private BigDecimal price;
}