package com.abdav.giri_guide.model.request;

import jakarta.validation.constraints.NotBlank;

public record HikingPointRequest(
        @NotBlank String name,
        @NotBlank String coordinate

) {
}
