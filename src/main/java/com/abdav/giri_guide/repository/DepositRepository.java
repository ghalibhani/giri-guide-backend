package com.abdav.giri_guide.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abdav.giri_guide.entity.Deposit;
import com.abdav.giri_guide.entity.TourGuide;

public interface DepositRepository extends JpaRepository<Deposit, String> {
    Optional<Deposit> findByTourGuideAndDeletedDateIsNull(TourGuide tourGuide);

}
