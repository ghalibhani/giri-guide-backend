package com.abdav.giri_guide.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abdav.giri_guide.entity.HikingPoint;
import com.abdav.giri_guide.entity.TourGuide;
import com.abdav.giri_guide.entity.TourGuideHikingPoint;

public interface TourGuideHikingPointRepository extends JpaRepository<TourGuideHikingPoint, String> {
    List<TourGuideHikingPoint> findByTourGuideAndDeletedDateIsNull(TourGuide tourGuide);

    List<TourGuideHikingPoint> findByHikingPointAndDeletedDateIsNull(HikingPoint hikingPoint);

    Optional<TourGuideHikingPoint> findByTourGuideAndHikingPointAndDeletedDateIsNull(
            TourGuide tourGuide, HikingPoint hikingPoint);
}
