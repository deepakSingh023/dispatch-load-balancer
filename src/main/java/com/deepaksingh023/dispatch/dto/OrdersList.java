package com.deepaksingh023.dispatch.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrdersList(

        @NotEmpty
        @Valid
        List<OrderRequest> orders
) {
}
