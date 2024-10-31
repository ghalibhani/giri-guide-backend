package com.abdav.giri_guide.service.impl;

import com.abdav.giri_guide.entity.AppUser;
import com.abdav.giri_guide.entity.User;
import com.abdav.giri_guide.repository.UserRepository;
import com.abdav.giri_guide.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public AppUser loadUserByUserId(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("Invalid Credential"));
        return AppUser.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole().getRole())
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Invalid Credential"));
        return AppUser.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole().getRole())
                .build();
    }
}
