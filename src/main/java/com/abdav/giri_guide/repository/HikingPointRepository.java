package com.abdav.giri_guide.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abdav.giri_guide.entity.HikingPoint;
import com.abdav.giri_guide.entity.Mountains;

public interface HikingPointRepository extends JpaRepository<HikingPoint, String> {
    List<HikingPoint> findByMountainAndDeletedDateIsNull(Mountains mountain);

    Optional<HikingPoint> findByIdAndDeletedDateIsNull(String id);

    List<HikingPoint> findByMountainIdAndDeletedDateIsNull(String mountainId);
}
