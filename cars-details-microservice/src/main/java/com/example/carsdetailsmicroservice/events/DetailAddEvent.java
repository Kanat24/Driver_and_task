package com.example.carsdetailsmicroservice.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.math.BigDecimal;

/**
 * The DetailAddEvent class represents an event for adding a detail to a car.
 * It contains information about the detail, such as serial number, price, license plate, driver ID, and currency.
 * This event is intended to be sent to Kafka for processing.
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DetailAddEvent {
    private String serialNumber;
    private BigDecimal price;
    private String licensePlate;
    private Long driverId;
    private String currency;
}