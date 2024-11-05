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
import com.abdav.giri_guide.model.response.TourGuideHikingPointActiveResponse;
import com.abdav.giri_guide.model.response.TourGuideListResponse;
import com.abdav.giri_guide.util.UrlUtil;

import jakarta.servlet.http.HttpServletRequest;

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

    public static List<TourGuideListResponse> toListOfTourGuideListResponse(
            List<TourGuideHikingPoint> listData, HttpServletRequest httpReq) {

        List<TourGuideListResponse> result = new ArrayList<>();
        for (TourGuideHikingPoint data : listData) {
            result.add(new TourGuideListResponse(
                    data.getTourGuide().getId(),
                    data.getTourGuide().getName(),
                    (data.getTourGuide().getImage() == null) ? null
                            : UrlUtil.resolveImageUrl(data.getTourGuide().getImage(), httpReq),
                    data.getTourGuide().getDescription(),
                    5.0,
                    10));
        }
        return result;
    }

    public static List<TourGuideHikingPointActiveResponse> toListOfTourGuideHikingPointActiveResponse(
            List<TourGuideHikingPoint> hikingPoints) {

        List<TourGuideHikingPointActiveResponse> result = new ArrayList<>();
        for (TourGuideHikingPoint hikingPoint : hikingPoints) {
            result.add(new TourGuideHikingPointActiveResponse(
                    hikingPoint.getId(),
                    hikingPoint.getHikingPoint().getMountain().getName(),
                    hikingPoint.getHikingPoint().getName(),
                    hikingPoint.isActive()));
        }

        return result;

    }
}
