package com.abdav.giri_guide.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.abdav.giri_guide.constant.EDepositStatus;
import com.abdav.giri_guide.constant.ETransactionStatus;
import com.abdav.giri_guide.entity.TourGuide;
import com.abdav.giri_guide.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    Page<Transaction> findAllByStatus(ETransactionStatus status, Pageable pageable);

    Page<Transaction> findAllByDeletedDateIsNotNull(Pageable pageable);

    Page<Transaction> findAllByCustomerIdAndStatusInAndDeletedDateIsNullOrderByStartDateAsc(
            String customerId, List<ETransactionStatus> status, Pageable pageable);

    List<Transaction> findAllByCustomerIdAndStatusInAndDeletedDateIsNullOrderByStartDateAsc(
            String customerId, List<ETransactionStatus> status);

    Page<Transaction> findAllByTourGuideIdAndStatusInAndDeletedDateIsNullOrderByStartDateAsc(
            String tourGuideId, List<ETransactionStatus> status, Pageable pageable);

    List<Transaction> findAllByTourGuideIdAndStatusInAndDeletedDateIsNullOrderByStartDateAsc(
            String tourGuideId, List<ETransactionStatus> status);

    Optional<Transaction> findFirstByTourGuideAndStatusOrderByStartDateAsc(
            TourGuide tourGuide, ETransactionStatus status);
}
