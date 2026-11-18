package com.deepaksingh023.dispatch.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record VehicleList(

        @Valid
        @NotNull
        List<VehicleRequest> vehicles
) {
}
