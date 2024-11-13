package com.abdav.giri_guide.model.request;

import com.abdav.giri_guide.constant.Message;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Email(message = Message.INVALID_EMAIL)
        @NotBlank(message = Message.REQUIRED_EMAIL)
        String email,

        @NotBlank(message = Message.REQUIRED_PASSWORD)
        String password
) {
}
