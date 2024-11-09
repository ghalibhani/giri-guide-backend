package com.abdav.giri_guide.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.abdav.giri_guide.entity.Customer;
import com.abdav.giri_guide.entity.GuideReview;
import com.abdav.giri_guide.entity.TourGuide;
import com.abdav.giri_guide.entity.Transaction;

public interface GuideReviewRepository extends JpaRepository<GuideReview, String> {
    Page<GuideReview> findByTourGuideAndDeletedDateIsNull(TourGuide tourGuide, Pageable page);

    Page<GuideReview> findByTourGuideAndReviewIsNotNullAndDeletedDateIsNull(TourGuide tourGuide, Pageable page);

    Optional<GuideReview> findByTourGuideAndCustomerAndDeletedDateIsNull(TourGuide tourGuide, Customer customer);

    Optional<GuideReview> findByTransactionAndDeletedDateIsNull(Transaction transaction);
}
