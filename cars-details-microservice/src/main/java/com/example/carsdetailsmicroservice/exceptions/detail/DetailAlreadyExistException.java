package com.example.carsdetailsmicroservice.exceptions.detail;

import com.example.carsdetailsmicroservice.exceptions.AppException;
import com.example.carsdetailsmicroservice.exceptions.ErrorCode;
import lombok.Getter;

/**
 * The DetailAlreadyExistException is an exception class that is thrown when a detail
 * already exists.
 * It extends the RuntimeException class and implements the AppException interface.
 * The exception includes an error code and parameters associated with the error.
 */
@Getter
public class DetailAlreadyExistException extends RuntimeException implements AppException {
  private ErrorCode errorCode;
  private Object[] params;

  public DetailAlreadyExistException(ErrorCode errorCode, Object... params) {
    this.errorCode = errorCode;
    this.params = params;
  }

  public DetailAlreadyExistException(String message) {
    super(message);
  }
}