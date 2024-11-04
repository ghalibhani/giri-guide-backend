package com.abdav.giri_guide.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.abdav.giri_guide.entity.HikingPoint;
import com.abdav.giri_guide.entity.Mountains;
import com.abdav.giri_guide.entity.TourGuideHikingPoint;
import com.abdav.giri_guide.model.response.HikingPointResponse;
import com.abdav.giri_guide.model.response.MountainListHikingPointResponse;

public class TourGuideHikingPointMapper {
    private TourGuideHikingPointMapper() {
    }

    public static List<MountainListHikingPointResponse> toListMountainListHikingPoint(List<TourGuideHikingPoint> data) {
        Map<Mountains, List<HikingPoint>> mountainMap = new HashMap<>();
        for (TourGuideHikingPoint tourGuideHikingPoint : data) {
            Mountains mountain = tourGuideHikingPoint.getHikingPoint().getMountain();
            if (mountainMap.containsKey(mountain)) {
                mountainMap.get(mountain).add(tourGuideHikingPoint.getHikingPoint());
            } else {
                mountainMap.put(mountain, new ArrayList<>(List.of(tourGuideHikingPoint.getHikingPoint())));
            }
        }

        List<MountainListHikingPointResponse> result = new ArrayList<>();
        for (Mountains mountain : mountainMap.keySet()) {
            List<HikingPointResponse> hikingPoints = new ArrayList<>();
            for (HikingPoint hikingPoint : mountainMap.get(mountain)) {
                hikingPoints.add(new HikingPointResponse(
                        hikingPoint.getId(),
                        hikingPoint.getMountain().getId(),
                        hikingPoint.getName(),
                        hikingPoint.getCoordinate(),
                        hikingPoint.getPrice()));
            }

            result.add(new MountainListHikingPointResponse(
                    mountain.getId(),
                    mountain.getName(),
                    mountain.getPriceSimaksi(),
                    hikingPoints));
        }

        return result;
    }
}
