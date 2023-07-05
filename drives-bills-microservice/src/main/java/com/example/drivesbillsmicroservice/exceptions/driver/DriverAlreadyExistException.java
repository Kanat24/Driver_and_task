package com.example.drivesbillsmicroservice.exceptions.driver;

import com.example.drivesbillsmicroservice.exceptions.AppException;
import com.example.drivesbillsmicroservice.exceptions.ErrorCode;
import lombok.Getter;

/**
 * Exception thrown when a driver already exists.
 */
@Getter
public class DriverAlreadyExistException extends RuntimeException implements AppException {
  private ErrorCode errorCode;
  private Object[] params;

  public DriverAlreadyExistException(ErrorCode errorCode, Object... params) {
    this.errorCode = errorCode;
    this.params = params;
  }

  public DriverAlreadyExistException(String message) {
    super(message);
  }
}
