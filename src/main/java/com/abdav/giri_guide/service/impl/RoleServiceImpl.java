package com.abdav.giri_guide.service.impl;

import com.abdav.giri_guide.entity.Role;
import com.abdav.giri_guide.repository.RoleRepository;
import com.abdav.giri_guide.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role getOrSaveRole(Role role) {
        Optional<Role> findRole = roleRepository.findByRole(role.getRole());
        return findRole.orElseGet(() -> roleRepository.saveAndFlush(role));
    }
}
