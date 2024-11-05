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
    Page<Transaction> findAllByDeletedDateIsNull(Pageable pageable);
    Page<Transaction> findAllByDeletedDateIsNotNull(Pageable pageable);

    Page<Transaction> findAllByStatusInAndDeletedDateIsNull(List<ETransactionStatus> status, Pageable pageable);
}
