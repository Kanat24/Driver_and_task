package com.example.drivesbillsmicroservice.exceptions.driver;

import com.example.drivesbillsmicroservice.exceptions.AppException;
import com.example.drivesbillsmicroservice.exceptions.ErrorCode;
import lombok.Getter;

/**
 * Exception thrown when a driver is not found.
 */
@Getter
public class DriverNotFoundException extends RuntimeException implements AppException {
  private ErrorCode errorCode;
  private Object[] params;

  public DriverNotFoundException(ErrorCode errorCode, Object... params) {
    this.errorCode = errorCode;
    this.params = params;
  }

  public DriverNotFoundException(String message) {
    super(message);
  }
}
