package com.lovelyglam.authserver.service;

import com.lovelyglam.database.model.dto.request.OTPVerifyRequest;

public interface OTPService {
    void verifyOTP (OTPVerifyRequest request);
    Long generateOTPCode(String identity);
}
