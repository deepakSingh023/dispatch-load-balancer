package com.deepaksingh023.dispatch.dto;

import com.deepaksingh023.dispatch.enums.Priority;

public record OrderResponse(
        String orderId,
        Double latitude,
        Double longitude,
        String address,
        Integer packageWeight,
        Priority priority
) {
}
