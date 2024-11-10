package com.abdav.giri_guide.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.abdav.giri_guide.constant.EDepositStatus;
import com.abdav.giri_guide.entity.Deposit;
import com.abdav.giri_guide.entity.DepositHistory;

public interface DepositHistoryRepository extends JpaRepository<DepositHistory, String> {
    Page<DepositHistory> findByDepositAndStatusInOrderByCreatedDateDesc(
            Deposit deposit, List<EDepositStatus> status, Pageable pageable);

    Page<DepositHistory> findByStatusInOrderByCreatedDateDesc(
            List<EDepositStatus> status, Pageable pageable);

    Page<DepositHistory> findByStatusInAndDepositTourGuideNameContainsIgnoreCaseOrderByCreatedDateDesc(
            List<EDepositStatus> status, String name, Pageable pageable);

    List<DepositHistory> findByDepositAndStatusInAndCreatedDateBetween(
            Deposit deposit,
            List<EDepositStatus> status,
            LocalDateTime createdDateStart,
            LocalDateTime createdDateEnd

    );

}
