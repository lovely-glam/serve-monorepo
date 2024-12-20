package com.lovelyglam.database.model.dto.request;

import com.lovelyglam.database.model.constant.OTPType;

import lombok.Data;

@Data
public class OTPVerifyRequest {
    private String otp;
    private String identity;
    private OTPType type;
}
