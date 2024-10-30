package com.abdav.giri_guide.dto.request;

import com.abdav.giri_guide.constant.Message;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    @NotBlank(message = Message.REQUIRED_DATA)
    private String fullName;

    @Email(message = Message.INVALID_EMAIL)
    @NotBlank(message = Message.REQUIRED_DATA)
    private String email;

    @NotBlank(message = Message.REQUIRED_DATA)
    @Size(min = 8, message = Message.INVALID_PASSWORD)
    private String password;

    private Date birthDate;

    @NotBlank(message = Message.REQUIRED_DATA)
    @Pattern(regexp = "\\d{16}", message = Message.INVALID_NIK)
    private String nik;

    @NotBlank(message = Message.REQUIRED_DATA)
    private String address;

    @NotBlank(message = Message.REQUIRED_DATA)
    private String gender;
}
