package com.abdav.giri_guide.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.abdav.giri_guide.entity.LocationRoute;

public interface LocationRouteRepository extends JpaRepository<LocationRoute, String> {
    Page<LocationRoute> findByTitleContainingIgnoreCaseAndDeletedDateIsNull(String title, Pageable pageable);
}
