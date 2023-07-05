package com.example.carsdetailsmicroservice.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Data Transfer Object (DTO) for a car event.
 * This class represents the payload for a car event that is sent to Kafka.
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