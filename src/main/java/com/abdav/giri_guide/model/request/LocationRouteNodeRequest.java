package com.abdav.giri_guide.model.request;

import jakarta.validation.constraints.NotNull;

public record LocationRouteNodeRequest(
                @NotNull String from,
                @NotNull String to,
                @NotNull String transportation,
                @NotNull String estimate,
                @NotNull String distance

) {
}
