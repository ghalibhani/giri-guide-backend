package com.abdav.giri_guide.model.request;

import java.util.Date;

public record HikerDetailRequest(
        String fullName,
        String nik,
        Date birthDate
) {
}
