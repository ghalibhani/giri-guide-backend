package com.abdav.giri_guide.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abdav.giri_guide.entity.ImageEntity;

public interface ImageRepository extends JpaRepository<ImageEntity, String> {
    Optional<ImageEntity> findByPath(String path);

    Optional<ImageEntity> findByName(String name);
}
