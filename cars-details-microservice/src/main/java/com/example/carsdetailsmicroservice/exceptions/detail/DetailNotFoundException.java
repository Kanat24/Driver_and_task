package com.example.carsdetailsmicroservice.exceptions.detail;

import com.example.carsdetailsmicroservice.exceptions.AppException;
import com.example.carsdetailsmicroservice.exceptions.ErrorCode;
import lombok.Getter;

/**
 * The DetailNotFoundException is an exception class that is thrown when a detail is not found.
 * It extends the RuntimeException class and implements the AppException interface.
 * The exception includes an error code and parameters associated with the error.
 */
@Getter
public class DetailNotFoundException extends RuntimeException implements AppException {
  private ErrorCode errorCode;
  private Object[] params;

  public DetailNotFoundException(ErrorCode errorCode, Object... params) {
    this.errorCode = errorCode;
    this.params = params;
  }

  public DetailNotFoundException(String message) {
    super(message);
  }
}