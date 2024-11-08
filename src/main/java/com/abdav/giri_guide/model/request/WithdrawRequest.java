package com.abdav.giri_guide.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record WithdrawRequest(
        @NotNull @Min(20000) Long nominal,
        @NotBlank String message) {
}
