package com.abdav.giri_guide.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abdav.giri_guide.entity.TourGuide;

public interface TourGuideRepository extends JpaRepository<TourGuide, String> {
    Optional<TourGuide> findByNikAndDeletedDateIsNull(String nik);
}
