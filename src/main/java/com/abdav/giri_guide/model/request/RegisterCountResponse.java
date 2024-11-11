package com.abdav.giri_guide.model.request;

import java.time.YearMonth;
import java.util.Map;

public record RegisterCountResponse(
        Map<YearMonth, Long> countRegister
) {
}
