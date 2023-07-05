package com.example.drivesbillsmicroservice.exceptions;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a custom error response.
 * Contains message, code, and time information about the error.
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