package com.lovelyglam.authserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lovelyglam.authserver.service.OTPService;
import com.lovelyglam.database.model.dto.request.OTPVerifyRequest;
import com.lovelyglam.database.model.dto.response.ResponseObject;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "otp")
@RequiredArgsConstructor
public class OTPVerifyController {
    private final OTPService otpService;
    @PostMapping(path = "verify")
    public ResponseEntity<ResponseObject> verifyOTP (@RequestBody OTPVerifyRequest request) {
        otpService.verifyOTP(request);
        return ResponseEntity.ok(ResponseObject.builder()
        .code("VERIFY_SUCCESS")
        .content(request)
        .message("Verify OTP Success")
        .isSuccess(true)
        .build());
    }
}
