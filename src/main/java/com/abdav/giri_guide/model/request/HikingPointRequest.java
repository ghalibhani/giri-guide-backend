package com.abdav.giri_guide.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record HikingPointRequest(
        @NotBlank String name,
        @NotBlank String coordinate,
        @NotNull @Min(0) Long price

) {
}
