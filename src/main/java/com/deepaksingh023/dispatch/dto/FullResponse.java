package com.deepaksingh023.dispatch.dto;

import java.util.List;

public record FullResponse(
        List<PlanResponse> dispatchPlan,

        List<UnAssignedOrderResponse> unassignedOrders

) {
}
