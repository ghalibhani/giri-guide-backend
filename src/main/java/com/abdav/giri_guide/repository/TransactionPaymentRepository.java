package com.abdav.giri_guide.repository;

import com.abdav.giri_guide.entity.TransactionPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionPaymentRepository extends JpaRepository<TransactionPayment, String> {
}
