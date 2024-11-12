package com.abdav.giri_guide.model.request;

import com.abdav.giri_guide.constant.Message;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Date;

public record RegisterRequest(
        @NotBlank(message = "fullName "+ Message.REQUIRED_DATA)
        String fullName,

        @Email(message = Message.INVALID_EMAIL)
        @NotBlank(message = "Email "+Message.REQUIRED_DATA)
        String email,

        @NotBlank(message ="Password " + Message.REQUIRED_DATA)
        @Size(min = 8, message = Message.INVALID_PASSWORD)
        String password,

        Date birthDate,

        @NotBlank(message ="NIK " + Message.REQUIRED_DATA)
        @Pattern(regexp = "\\d{16}", message = Message.INVALID_NIK)
        String nik,

        @NotBlank(message ="Alamat " + Message.REQUIRED_DATA)
        String address,

        @NotBlank(message ="Gender " + Message.REQUIRED_DATA)
        String gender
) {
}
