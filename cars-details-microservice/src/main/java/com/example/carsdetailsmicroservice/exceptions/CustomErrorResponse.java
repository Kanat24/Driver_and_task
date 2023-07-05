package com.example.carsdetailsmicroservice.exceptions;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The CustomErrorResponse class represents a custom error response.
 * It contains the error message, error code, and the timestamp when the error occurred.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomErrorResponse {
  private String message;
  private String code;
  private LocalDateTime time;
}