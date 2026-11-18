package com.deepaksingh023.dispatch.dto;

public record PlanResponse(

        String vehicleId,
        Integer totalLoad,
        Double totalDistance,
        AssignedOrders assignedOrders
) {
}
