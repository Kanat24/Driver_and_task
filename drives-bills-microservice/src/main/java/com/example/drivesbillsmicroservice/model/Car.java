package com.example.drivesbillsmicroservice.model;
import com.example.drivesbillsmicroservice.entity.Driver;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Car {
    private String vin;
    private String licensePlate;
    private String manufacturer;
    private String model;
    private Integer year;
    private Set<Detail> details = new HashSet<>();
    private Driver driver;
}