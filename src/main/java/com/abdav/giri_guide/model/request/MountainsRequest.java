package com.abdav.giri_guide.model.request;

import com.abdav.giri_guide.constant.EMountainStatus;
import com.abdav.giri_guide.entity.Mountains;

import jakarta.validation.constraints.NotBlank;

public record MountainsRequest(
        @NotBlank String name,
        @NotBlank String city,
        String description,
        String status,
        String message,
        Boolean useSimaksi,
        Long priceSimaksi,
        String tips,
        String bestTime

) {
    public EMountainStatus statusToEnum() {
        EMountainStatus mountainStatus;
        switch (status.trim().toLowerCase()) {
            case "normal":
                mountainStatus = EMountainStatus.NORMAL;
                break;

            case "waspada":
                mountainStatus = EMountainStatus.WASPADA;
                break;

            case "siaga":
                mountainStatus = EMountainStatus.SIAGA;
                break;

            case "awas":
                mountainStatus = EMountainStatus.AWAS;
                break;

            default:
                mountainStatus = EMountainStatus.NORMAL;
                break;
        }
        return mountainStatus;
    }

    public Mountains toMountains() {

        return Mountains.builder()
                .name(name.trim())
                .city(city.trim())
                .description(description.trim())
                .status(statusToEnum())
                .message(message.trim())
                .useSimaksi(useSimaksi)
                .priceSimaksi(priceSimaksi)
                .tips(tips)
                .bestTime(bestTime)
                .build();
    }
}
