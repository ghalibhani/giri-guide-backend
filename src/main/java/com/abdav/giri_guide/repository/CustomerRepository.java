package com.abdav.giri_guide.repository;

import com.abdav.giri_guide.entity.Customer;
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
    List<Customer> findAllByDeletedDateIsNull();

    Optional<Customer> findByIdAndDeletedDateIsNull(String id);
    Optional<Customer> findByIdAndDeletedDateIsNotNull(String id);

    Optional<Customer> findByUserIdAndDeletedDateIsNull(String userId);
    Customer findByUserIdAndDeletedDateIsNotNull(String userId);

    Optional<Customer> findByNikAndDeletedDateIsNull(String nik);
}
