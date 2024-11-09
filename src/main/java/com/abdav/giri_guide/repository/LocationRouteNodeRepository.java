package com.abdav.giri_guide.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abdav.giri_guide.entity.LocationRouteNode;

public interface LocationRouteNodeRepository extends JpaRepository<LocationRouteNode, String> {
}
