package com.abdav.giri_guide.service.impl;

import java.util.Optional;

import com.abdav.giri_guide.model.request.LoginRequest;
import com.abdav.giri_guide.model.request.RegisterRequest;
import com.abdav.giri_guide.model.response.LoginResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.abdav.giri_guide.constant.EGender;
import com.abdav.giri_guide.constant.ERole;
import com.abdav.giri_guide.constant.Message;
import com.abdav.giri_guide.entity.AppUser;
import com.abdav.giri_guide.entity.Customer;
import com.abdav.giri_guide.entity.Role;
import com.abdav.giri_guide.entity.User;
import com.abdav.giri_guide.repository.CustomerRepository;
import com.abdav.giri_guide.repository.TourGuideRepository;
import com.abdav.giri_guide.repository.UserRepository;
import com.abdav.giri_guide.security.JwtUtil;
import com.abdav.giri_guide.service.AuthService;
import com.abdav.giri_guide.service.CustomerService;
import com.abdav.giri_guide.service.RoleService;
import com.abdav.giri_guide.util.ValidationUtil;

import jakarta.annotation.PostConstruct;
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

    private final CustomerRepository customerRepository;
    private final TourGuideRepository tourGuideRepository;

    @Value("${app.giri-guide.email-admin}")
    private String emailAdmin;

    @Value("${app.giri-guide.password-admin}")
    private String passwordAdmin;

    @PostConstruct
    private void initAdmin() {
        Optional<User> admin = userRepository.findByEmail(emailAdmin);
        if (admin.isPresent()) {
            return;
        }

        Role role = roleService.getOrSaveRole(Role.builder().role(ERole.ROLE_ADMIN).build());

        User user = User.builder()
                .email(emailAdmin)
                .password(passwordEncoder.encode(passwordAdmin))
                .role(role)
                .build();
        userRepository.saveAndFlush(user);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterRequest registerRequest) {
        if (userRepository.findByEmailAndDeletedDateIsNull(registerRequest.email()).isPresent() ||
                customerRepository.findByNikAndDeletedDateIsNull(registerRequest.nik()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email atau nik sudah terdaftar");
        }
        validationUtil.validate(registerRequest);
        Role role = roleService.getOrSaveRole(Role.builder().role(ERole.ROLE_CUSTOMER).build());

        User user = User.builder()
                .email(registerRequest.email())
                .password(passwordEncoder.encode(registerRequest.password()))
                .role(role)
                .build();
        userRepository.saveAndFlush(user);

        Customer customer = Customer.builder()
                .fullName(registerRequest.fullName())
                .birthDate(registerRequest.birthDate())
                .nik(registerRequest.nik())
                .address(registerRequest.address())
                .gender(EGender.valueOf(registerRequest.gender().toUpperCase()))
                .user(user)
                .build();
        customerService.createCustomer(customer);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        validationUtil.validate(loginRequest);
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.email(),
                    loginRequest.password()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            AppUser appUser = (AppUser) authentication.getPrincipal();
            String token = jwtUtil.generateToken(appUser);

            String name = null;
            if (appUser.getRole().equals(ERole.ROLE_CUSTOMER)) {
                name = customerRepository.findByUserIdAndDeletedDateIsNull(appUser.getId()).get().getFullName();

            } else if (appUser.getRole().equals(ERole.ROLE_GUIDE)) {
                User user = userRepository.findByEmail(loginRequest.email()).get();
                name = tourGuideRepository.findByUsersAndDeletedDateIsNull(user).get().getName();

            }

            return new LoginResponse(
                    token,
                    appUser.getId(),
                    appUser.getEmail(),
                    appUser.getRole(),
                    name
            );
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
    }

    @Override
    public void changePassword(String userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Message.DATA_NOT_FOUND));
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Password lama salah");
        }

        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.saveAndFlush(user);
    }
}
