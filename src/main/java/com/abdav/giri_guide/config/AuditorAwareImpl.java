package com.abdav.giri_guide.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() instanceof String) {
            return Optional.of("System");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return Optional.of(userDetails.getUsername());
//        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
//            return Optional.of(authentication.getName());
//        } else {
//            return Optional.of(authentication.getPrincipal().toString());
//        }
    }
}
