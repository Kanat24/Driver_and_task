package com.example.carsdetailsmicroservice.exceptions;

import com.example.carsdetailsmicroservice.constants.ErrorCodeConstants;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * The ErrorCode enum represents the error codes and corresponding HTTP status for
 * different error scenarios.
 * Each error code is associated with a unique code, HTTP status, and timestamp
 * when the error occurred.
 */
@Getter
public enum ErrorCode {
  CAR_ALREADY_EXIST(ErrorCodeConstants.CAR_ALREADY_EXIST_409,
            HttpStatus.CONFLICT,
            LocalDateTime.now()),
  CAR_NOT_FOUND(ErrorCodeConstants.CAR_NOT_FOUND_404,
            HttpStatus.NOT_FOUND,
            LocalDateTime.now()),
  DETAIL_NOT_FOUND(ErrorCodeConstants.DETAIL_NOT_FOUND_404,
            HttpStatus.NOT_FOUND,
            LocalDateTime.now()),
  DETAIL_ALREADY_EXIST(ErrorCodeConstants.DETAIL_ALREADY_EXIST_409,
            HttpStatus.CONFLICT,
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