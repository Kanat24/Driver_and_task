package com.example.carsdetailsmicroservice.exceptions.car;

import com.example.carsdetailsmicroservice.exceptions.AppException;
import com.example.carsdetailsmicroservice.exceptions.ErrorCode;
import lombok.Getter;

/**
 * The CarNotFoundException is an exception class that is thrown when a car is not found.
 * It extends the RuntimeException class and implements the AppException interface.
 * The exception includes an error code and parameters associated with the error.
 */
@Getter
public class CarNotFoundException extends RuntimeException implements AppException {
  private ErrorCode errorCode;
  private Object[] params;

  public CarNotFoundException(ErrorCode errorCode, Object... params) {
    this.errorCode = errorCode;
    this.params = params;
  }

  public CarNotFoundException(String message) {
    super(message);
  }
}