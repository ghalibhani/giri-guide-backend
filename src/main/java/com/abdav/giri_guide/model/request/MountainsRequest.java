package com.abdav.giri_guide.model.request;

import com.abdav.giri_guide.entity.Mountains;

public record MountainsRequest(
        String name,
        String city,
        String description

) {
    public Mountains toMountains() {
        return Mountains.builder()
                .name(name)
                .city(city)
                .description(description)
                .build();
    }
}
