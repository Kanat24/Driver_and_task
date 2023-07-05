package com.example.drivesbillsmicroservice.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a car purchase event for kafka message.
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CarPurchaseEvent {
  private Long driverId;
  private String licensePlate;
}