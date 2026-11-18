package com.deepaksingh023.dispatch.dto;

import com.deepaksingh023.dispatch.enums.Priority;
import jakarta.validation.constraints.*;

public record OrderRequest(

        @NotBlank
        String orderId,

        @NotNull
        @DecimalMin("-90")
        @DecimalMax("90")
        Double latitude,

        @NotNull
        @DecimalMin("-180")
        @DecimalMax("180")
        Double longitude,

        @NotBlank
        String address,

        @NotNull
        @Positive
        Double packageWeight,

        @NotNull
        Priority priority
) {
}
