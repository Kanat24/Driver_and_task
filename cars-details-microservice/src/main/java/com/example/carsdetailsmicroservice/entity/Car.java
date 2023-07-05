package com.example.carsdetailsmicroservice.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The Car entity class represents a car entity in the system.
 * It contains information about a car, such as VIN, license plate,
 * manufacturer, model, year, details, and driver ID.
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Car {
  @Id
  @Column(unique = true)
  private String vin;
  @Column(unique = true)
  private String licensePlate;
  private String manufacturer;
  private String model;
  private Integer year;
  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(
          name = "car_details",
          joinColumns = @JoinColumn(name = "car_vin"),
          inverseJoinColumns = @JoinColumn(name = "detail_id")
    )
  private Set<Detail> details = new HashSet<>();
  private Long driverId;
}