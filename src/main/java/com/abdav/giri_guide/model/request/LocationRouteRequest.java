package com.abdav.giri_guide.model.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LocationRouteRequest(
        @NotBlank String title,
        @NotBlank String description,
        @NotNull List<LocationRouteNodeRequest> routes

) {
}
