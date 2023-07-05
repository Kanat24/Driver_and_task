package com.example.drivesbillsmicroservice.entity;

import com.example.drivesbillsmicroservice.enums.LicenseCategory;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a driver entity.
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table
public class Driver {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column
  private String firstName;
  private String lastName;
  private String passport;
  @Enumerated(EnumType.STRING)
  private LicenseCategory licenseCategory;
  private LocalDate dateOfBirth;
  private int experience;
  @OneToOne(mappedBy = "driver", cascade = CascadeType.ALL, orphanRemoval = true)
  private Account account;
  private Long carId;
}