package com.lovelyglam.authserver.service;

import com.lovelyglam.database.model.dto.request.OTPVerifyRequest;
import com.lovelyglam.database.model.other.OTPKey;

public interface OTPService {
    void verifyOTP (OTPVerifyRequest request);
    void generateOTPCode(OTPKey key, String username, String other);
}
