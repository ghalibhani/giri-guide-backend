package com.abdav.giri_guide.model.response;

import java.util.Date;

public record HikerDetailResponse(
        String fullName,
        String nik,
        Date bithDate
) {
}
