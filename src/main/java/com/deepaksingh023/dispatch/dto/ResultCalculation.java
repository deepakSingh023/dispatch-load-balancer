package com.deepaksingh023.dispatch.dto;


import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResultCalculation {

    private String vehicleId;

    private Integer totalLoad;

    private Double totalDistanceTravelled;

    private AssignedOrders assignedOrders;

    private Double currentLat;

    private Double currentLon;
}
