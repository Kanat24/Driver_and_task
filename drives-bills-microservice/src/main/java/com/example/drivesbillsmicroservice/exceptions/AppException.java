package com.example.drivesbillsmicroservice.exceptions;

public interface AppException {
  ErrorCode getErrorCode();

  Object[] getParams();
}
