package com.deepaksingh023.dispatch.dto;

import com.deepaksingh023.dispatch.enums.Status;

public record OrderSaveResponse(
        String message,
        Status status
) {
}
