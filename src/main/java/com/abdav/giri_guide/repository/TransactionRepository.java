package com.abdav.giri_guide.repository;

import com.abdav.giri_guide.constant.ETransactionStatus;
import com.abdav.giri_guide.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    Page<Transaction> findAllByStatus(ETransactionStatus status, Pageable pageable);
    Page<Transaction> findAllByDeletedDateIsNotNull(Pageable pageable);

    Page<Transaction> findAllByCustomerIdAndStatusInAndDeletedDateIsNullOrderByStartDateAsc(String customerId,List<ETransactionStatus> status, Pageable pageable);
    List<Transaction> findAllByCustomerIdAndStatusInAndDeletedDateIsNullOrderByStartDateAsc(String customerId,List<ETransactionStatus> status);

    Page<Transaction> findAllByTourGuideIdAndStatusInAndDeletedDateIsNullOrderByStartDateAsc(String tourGuideId,List<ETransactionStatus> status, Pageable pageable);
    List<Transaction> findAllByTourGuideIdAndStatusInAndDeletedDateIsNullOrderByStartDateAsc(String tourGuideId,List<ETransactionStatus> status);
}
