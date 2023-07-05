package com.example.carsdetailsmicroservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Account {
    private Long id;
    private Driver driver;
    private Double redDollar;
    private Double greenDollar;
    private Double blueDollar;
}