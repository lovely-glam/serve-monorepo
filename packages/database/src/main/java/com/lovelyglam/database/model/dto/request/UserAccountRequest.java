package com.lovelyglam.database.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAccountRequest {
    @Email(message = "Email must not be empty")
    private String email;
    @NotBlank(message = "Name must not be empty")
    private String fullName;
    @NotBlank(message = "Name must not be empty")
    private String avatarUrl;
    @NotBlank(message = "Password must not be empty")
    private String password;
    @NotBlank(message = "Repassword must not be empty")
    private String rePassword;
}
