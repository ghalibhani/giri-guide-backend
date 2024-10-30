package com.abdav.giri_guide.service;

import com.abdav.giri_guide.entity.AppUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    AppUser loadUserByUserId(String userId);
}
