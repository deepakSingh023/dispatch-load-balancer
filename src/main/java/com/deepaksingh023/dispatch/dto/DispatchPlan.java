package com.deepaksingh023.dispatch.dto;

import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

public record DispatchPlan(

        List<PlanResponse> planResponseList
) {
}
