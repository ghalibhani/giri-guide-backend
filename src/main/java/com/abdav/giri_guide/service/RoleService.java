package com.abdav.giri_guide.service;

import com.abdav.giri_guide.entity.Role;

public interface RoleService {
    Role getOrSaveRole(Role role);
}
