package com.deepaksingh023.dispatch.dto;

import java.time.Instant;


public record ErrorResponse(
        String error,
        String message,
        Instant timestamp
) {
}
