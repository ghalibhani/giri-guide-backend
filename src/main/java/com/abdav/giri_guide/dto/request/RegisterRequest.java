package com.abdav.giri_guide.dto.request;

import com.abdav.giri_guide.constant.Message;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = Message.REQUIRED_FULLNAME)
    private String fullName;

    @Email(message = Message.INVALID_EMAIL)
    @NotBlank(message = Message.REQUIRED_EMAIL)
    private String email;

    @NotBlank(message = Message.REQUIRED_PASSWORD)
    private String password;

    @NotBlank(message = Message.REQUIRED_BIRTHDATE)
    private Date birthDate;

    @NotBlank(message = Message.REQUIRED_NIK)
    private String nik;

    @NotBlank(message = Message.REQUIRED_ADDRESS)
    private String address;

    private String gender;
}
