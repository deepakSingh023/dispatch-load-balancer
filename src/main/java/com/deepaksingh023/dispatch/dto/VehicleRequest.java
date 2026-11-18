package com.deepaksingh023.dispatch.dto;

import jakarta.validation.constraints.*;

public record VehicleRequest(

        @NotBlank
        String vehicleId,

        @NotNull
        @Positive
        Integer capacity,

        @NotNull
        @DecimalMax("90")
        @DecimalMin("-90")
        Double currentLatitude,


        @NotNull
        @DecimalMax("180")
        @DecimalMin("-180")
        Double currentLongitude,

        @NotBlank
        String currentAddress
) {
}
