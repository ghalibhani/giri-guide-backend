package com.abdav.giri_guide.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GuideReviewPutRequest(
                @Min(1) @Max(2) @NotNull Integer rating,
                @NotBlank String review

) {
}
