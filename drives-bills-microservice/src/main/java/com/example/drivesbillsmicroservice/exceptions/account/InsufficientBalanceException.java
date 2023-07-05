package com.example.drivesbillsmicroservice.exceptions.account;

import com.example.drivesbillsmicroservice.exceptions.AppException;
import com.example.drivesbillsmicroservice.exceptions.ErrorCode;
import lombok.Getter;

/**
 * Exception thrown when there is insufficient balance in an account.
 */
@Getter
public class InsufficientBalanceException extends RuntimeException implements AppException {
  private ErrorCode errorCode;
  private Object[] params;

  public InsufficientBalanceException(ErrorCode errorCode, Object... params) {
    this.errorCode = errorCode;
    this.params = params;
  }

  public InsufficientBalanceException(String message) {
    super(message);
  }
}

