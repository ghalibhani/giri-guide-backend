package com.abdav.giri_guide.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.abdav.giri_guide.entity.Mountains;
import com.abdav.giri_guide.model.response.MountainsListResponse;

public interface MountainsRepository extends JpaRepository<Mountains, String> {
    List<Mountains> findAllByCity(String city);

    Optional<Mountains> findByNameIgnoreCaseAndDeletedDateIsNull(String name);

    Page<MountainsListResponse> findAllByCityIgnoreCaseAndDeletedDateIsNull(String city, Pageable page);

    Page<MountainsListResponse> findAllByDeletedDateIsNull(Pageable page);

    Page<MountainsListResponse> findAllByDeletedDateIsNotNull(Pageable page);

}
