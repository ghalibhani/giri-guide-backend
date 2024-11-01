package com.abdav.giri_guide.repository;

import com.abdav.giri_guide.entity.Customer;
import com.abdav.giri_guide.model.response.CustomerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    Page<Customer> findAllByDeletedDateIsNull(Pageable pageable);
    Page<Customer> findAllByDeletedDateIsNotNull(Pageable pageable);

    Optional<Customer> findByIdAndDeletedDateIsNull(String id);
    Optional<Customer> findByIdAndDeletedDateIsNotNull(String id);
}
