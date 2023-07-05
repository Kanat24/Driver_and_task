package com.example.carsdetailsmicroservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDate;
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Driver {
    private Long id;
    private String firstName;
    private String lastName;
    private String passport;
    private String licenseCategory;
    private LocalDate dateOfBirth;
    private int experience;
    private Account account;
}
