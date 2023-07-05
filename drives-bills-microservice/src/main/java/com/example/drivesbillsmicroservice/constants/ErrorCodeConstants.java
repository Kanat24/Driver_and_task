package com.example.drivesbillsmicroservice.constants;

/**
 * Constants class for error codes used in the application.
 * This class provides static final fields representing different error codes.
 * Error codes are used for identifying and handling specific types of errors or exceptions.
 */
public class ErrorCodeConstants {
  public static final String DRIVER_ALREADY_EXIST_409 = "409_DRIVER_ALREADY_EXIST";
  public static final String DRIVER_NOT_FOUND_401 = "401_DRIVER_NOT_FOUND";
  public static final String ACCOUNT_NOT_FOUND_401 = "401_ACCOUNT_NOT_FOUND";
  public static final String ACCOUNT_INSUFFICIENT_BALANCE_402 = "402_ACCOUNT_INSUFFICIENT_BALANCE";
}