package com.abdav.giri_guide.repository;

import com.abdav.giri_guide.entity.TransactionHiker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionHikerRepository extends JpaRepository<TransactionHiker, String> {
}
