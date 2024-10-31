package com.abdav.giri_guide.model.request;

import com.abdav.giri_guide.constant.EMountainStatus;
import com.abdav.giri_guide.entity.Mountains;

public record MountainsRequest(
        String name,
        String city,
        String description,
        String status,
        String message

) {
    public EMountainStatus statusToEnum() {
        EMountainStatus mountainStatus;
        switch (status.toLowerCase()) {
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
                .name(name)
                .city(city)
                .description(description)
                .status(statusToEnum())
                .message(message)
                .build();
    }
}
