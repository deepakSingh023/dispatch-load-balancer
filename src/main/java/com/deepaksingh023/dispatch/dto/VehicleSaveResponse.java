package com.deepaksingh023.dispatch.dto;

import com.deepaksingh023.dispatch.enums.Status;

public record VehicleSaveResponse(
        String message,
        Status status
) {
}
