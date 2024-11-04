package com.abdav.giri_guide.service.impl;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.abdav.giri_guide.constant.EGender;
import com.abdav.giri_guide.constant.ERole;
import com.abdav.giri_guide.dto.request.LoginRequest;
import com.abdav.giri_guide.dto.request.RegisterRequest;
import com.abdav.giri_guide.dto.response.LoginResponse;
import com.abdav.giri_guide.entity.AppUser;
import com.abdav.giri_guide.entity.Customer;
import com.abdav.giri_guide.entity.Role;
import com.abdav.giri_guide.entity.User;
import com.abdav.giri_guide.repository.UserRepository;
import com.abdav.giri_guide.security.JwtUtil;
import com.abdav.giri_guide.service.AuthService;
import com.abdav.giri_guide.service.CustomerService;
import com.abdav.giri_guide.service.RoleService;
import com.abdav.giri_guide.util.ValidationUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final CustomerService customerService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final ValidationUtil validationUtil;

    @Override
    public void register(RegisterRequest registerRequest) {
        try {
            validationUtil.validate(registerRequest);
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
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email atau nik sudah terdaftar");
        }
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        validationUtil.validate(loginRequest);
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            AppUser appUser = (AppUser) authentication.getPrincipal();
            String token = jwtUtil.generateToken(appUser);

            return LoginResponse.builder()
                    .token(token)
                    .UserId(appUser.getId())
                    .email(appUser.getEmail())
                    .role(appUser.getRole())
                    .build();
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
    }
}
