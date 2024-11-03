package com.abdav.giri_guide.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record HikingPointRequest(
        @NotBlank String name,
        @NotBlank String coordinate,
        @NotNull Double price

) {
}
