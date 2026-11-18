package com.deepaksingh023.dispatch.dto;

import java.util.List;

public record AssignedOrders(

        List<OrderResponse> orders
) {
}
