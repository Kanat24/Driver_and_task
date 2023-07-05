package com.example.drivesbillsmicroservice.exceptions;

import com.example.drivesbillsmicroservice.constants.ErrorCodeConstants;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Represents error codes for specific application exceptions.
 * Each enum value contains a code, HttpStatus, and LocalDateTime.
 */
@Getter
public enum ErrorCode {
  DRIVER_ALREADY_EXIST(ErrorCodeConstants.DRIVER_ALREADY_EXIST_409,
            HttpStatus.CONFLICT,
            LocalDateTime.now()),
  DRIVER_NOT_FOUND(ErrorCodeConstants.DRIVER_NOT_FOUND_401,
            HttpStatus.UNAUTHORIZED,
            LocalDateTime.now()),
  ACCOUNT_INSUFFICIENT_BALANCE(ErrorCodeConstants.ACCOUNT_INSUFFICIENT_BALANCE_402,
            HttpStatus.PAYMENT_REQUIRED,
            LocalDateTime.now()),
  ACCOUNT_NOT_FOUND(ErrorCodeConstants.ACCOUNT_NOT_FOUND_401,
                     HttpStatus.UNAUTHORIZED,
                     LocalDateTime.now());

  private String code;
  private HttpStatus httpStatus;
  private LocalDateTime localDateTime;

  ErrorCode(String code, HttpStatus httpStatus, LocalDateTime localDateTime) {
    this.code = code;
    this.httpStatus = httpStatus;
    this.localDateTime = localDateTime;
  }
}