package com.abdav.giri_guide.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.abdav.giri_guide.entity.GuideReview;

public interface GuideReviewRepository extends JpaRepository<GuideReview, String> {
    Page<GuideReview> findByGuideAndDeletedDateIsNull(String guide, Pageable page);

    Optional<GuideReview> findByGuideAndCustomerAndDeletedDateIsNull(String guide, String customer);
}
