package com.abdav.giri_guide.mapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.abdav.giri_guide.entity.HikingPoint;
import com.abdav.giri_guide.model.response.HikingPointResponse;

public class HikingPointMapper {
    private HikingPointMapper() {
    }

    public static Set<HikingPointResponse> toSetHikingPointResponse(List<HikingPoint> hikingPoints) {
        Set<HikingPointResponse> result = new HashSet<>();
        for (HikingPoint hikingPoint : hikingPoints) {
            result.add(new HikingPointResponse(
                    hikingPoint.getId(),
                    hikingPoint.getMountain().getId(),
                    hikingPoint.getName(),
                    hikingPoint.getCoordinate(),
                    hikingPoint.getPrice()));
        }
        return result;
    }
}
