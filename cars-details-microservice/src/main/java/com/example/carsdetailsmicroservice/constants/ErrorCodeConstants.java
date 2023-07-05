package com.example.carsdetailsmicroservice.constants;

/**
 * Constants class for error codes used in the application.
 * This class provides static final fields representing different error codes.
 * Error codes are used for identifying and handling specific types of errors or exceptions.
 */
public class ErrorCodeConstants {
  public static final String CAR_ALREADY_EXIST_409 = "409_CAR_ALREADY_EXIST";
  public static final String CAR_NOT_FOUND_404 = "404_CAR_NOT_FOUND";
  public static final String DETAIL_NOT_FOUND_404 = "404_DETAIL_NOT_FOUND";
  public static final String DETAIL_ALREADY_EXIST_409 = "409_DETAIL_ALREADY_EXIST";
}