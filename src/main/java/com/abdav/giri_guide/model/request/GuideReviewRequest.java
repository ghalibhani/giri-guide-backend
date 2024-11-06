package com.abdav.giri_guide.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GuideReviewRequest(
        @NotBlank String userId,
        @NotBlank String transactionId,
        @Min(1) @Max(5) Integer rating,
        @NotNull Boolean usePorter,
        String review

) {
}
