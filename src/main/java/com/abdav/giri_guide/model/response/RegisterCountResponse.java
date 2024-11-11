package com.abdav.giri_guide.model.response;

import java.time.YearMonth;

public record RegisterCountResponse(
        YearMonth yearMonth,
        Long registerCount
) {
}
