package com.lovelyglam.database.model.dto.request;

import lombok.Data;

@Data
public class OTPVerifyRequest {
    private Long otp;
    private String role;
}
