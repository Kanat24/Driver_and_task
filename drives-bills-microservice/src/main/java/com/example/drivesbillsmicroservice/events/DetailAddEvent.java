package com.example.drivesbillsmicroservice.events;

import lombok.*;

import java.math.BigDecimal;

/**
 * Represents a detail add event for kafka message.
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
