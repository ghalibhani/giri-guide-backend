package com.abdav.giri_guide.service.impl;

import com.abdav.giri_guide.dto.request.RegisterRequest;
import com.abdav.giri_guide.dto.response.CommonResponse;
import com.abdav.giri_guide.entity.*;
import com.abdav.giri_guide.repository.UserRepository;
import com.abdav.giri_guide.security.JwtUtil;
import com.abdav.giri_guide.service.AuthService;
import com.abdav.giri_guide.service.CustomerService;
import com.abdav.giri_guide.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final CustomerService customerService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    @Override
    public void register(RegisterRequest registerRequest) {
        try {
            Role role = roleService.getOrSaveRole(Role.builder().role(ERole.ROLE_CUSTOMER).build());

            User user = User.builder()
                    .email(registerRequest.getEmail())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .role(role)
                    .build();
            userRepository.saveAndFlush(user);

            Customer customer = Customer.builder()
                    .fullName(registerRequest.getFullName())
                    .birthDate(registerRequest.getBirthDate())
                    .nik(registerRequest.getNik())
                    .address(registerRequest.getAddress())
                    .gender(EGender.valueOf(registerRequest.getGender().toUpperCase()))
                    .user(user)
                    .build();
            customerService.createCustomer(customer);

        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        }
    }
}
