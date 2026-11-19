package com.deepaksingh023.dispatch.dto;

import com.deepaksingh023.dispatch.enums.Priority;

public record UnAssignedOrderResponse(
        String orderId,
        Double latitude,
        Double longitude,
        String address,
        Integer packageWeight,
        Priority priority,
        String reason
) {
}
