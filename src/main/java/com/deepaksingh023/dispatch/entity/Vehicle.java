package com.deepaksingh023.dispatch.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "vehicle_table")
@Getter
@Setter
public class Vehicle {

    @Id
    @Column(nullable = false , length = 50)
    private String vehicleId;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private Double currentLatitude;

    @Column(nullable = false)
    private Double currentLongitude;

    @Column(nullable = false , length = 500)
    private String currentAddress;
}
