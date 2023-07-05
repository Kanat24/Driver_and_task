package com.example.carsdetailsmicroservice.dto.message;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Data Transfer Object (DTO) for a message response.
 * This class represents the payload for a message response from an operation.
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MessageDto {
  @Schema(description = "Message after operation",
          example = "Some message")
  private String message;
}
