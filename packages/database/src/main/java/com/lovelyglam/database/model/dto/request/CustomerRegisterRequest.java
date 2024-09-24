package com.lovelyglam.database.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CustomerRegisterRequest {
    @NotBlank(message = "Username must not be empty")
    private String username;
    @Email(message = "Email must not be empty")
    private String email;
    @NotBlank(message = "Name must not be empty")
    private String fullName;
    @NotBlank(message = "Password must not be empty")
    private String password;
    @NotBlank(message = "Repassword must not be empty")
    private String rePassword;
}
