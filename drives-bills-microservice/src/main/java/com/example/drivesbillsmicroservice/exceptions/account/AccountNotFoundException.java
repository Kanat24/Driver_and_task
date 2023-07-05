package com.example.drivesbillsmicroservice.exceptions.account;

import com.example.drivesbillsmicroservice.exceptions.AppException;
import com.example.drivesbillsmicroservice.exceptions.ErrorCode;
import lombok.Getter;

/**
 * Exception thrown when an account is not found.
 */
@Getter
public class AccountNotFoundException extends RuntimeException implements AppException {
  private ErrorCode errorCode;
  private Object[] params;

  public AccountNotFoundException(ErrorCode errorCode, Object... params) {
    this.errorCode = errorCode;
    this.params = params;
  }

  public AccountNotFoundException(String message) {
    super(message);
  }
}
