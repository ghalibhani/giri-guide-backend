package com.abdav.giri_guide.repository;

import com.abdav.giri_guide.entity.TransactionPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionPaymentRepository extends JpaRepository<TransactionPayment, String> {
    Optional<TransactionPayment> findByTransactionId(String transactionId);
    List<TransactionPayment> findAllByDeletedDateIsNull();
}
