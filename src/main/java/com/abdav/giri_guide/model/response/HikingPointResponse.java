package com.abdav.giri_guide.model.response;

public record HikingPointResponse(
        String id,
        String mountainId,
        String name,
        String coordinate,
        Integer price

) {
}
