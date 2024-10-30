package com.abdav.giri_guide.dto.request;

import com.abdav.giri_guide.constant.Message;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {
    @Email(message = Message.INVALID_EMAIL)
    @NotBlank(message = Message.REQUIRED_EMAIL)
    private String email;

    @NotBlank(message = Message.REQUIRED_PASSWORD)
    private String password;
}
