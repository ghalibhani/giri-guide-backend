package com.abdav.giri_guide.repository;

import com.abdav.giri_guide.entity.ERole;
import com.abdav.giri_guide.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByRole(ERole name);
}
