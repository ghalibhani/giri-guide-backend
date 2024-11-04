package com.abdav.giri_guide.model.request;

import jakarta.validation.constraints.NotBlank;

public record UserIdRequest(
        @NotBlank String userId) {
}
