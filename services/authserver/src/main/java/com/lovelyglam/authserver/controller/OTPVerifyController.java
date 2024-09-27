package com.lovelyglam.authserver.controller;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lovelyglam.authserver.service.BusinessService;
import com.lovelyglam.authserver.service.OTPService;
import com.lovelyglam.database.model.dto.request.OTPVerifyRequest;
import com.lovelyglam.database.model.dto.response.ResponseObject;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "otp")
@RequiredArgsConstructor
public class OTPVerifyController {
    private final OTPService otpService;
    private final BusinessService businessService;
    @PatchMapping(path = "verify")
    public ResponseEntity<ResponseObject> verifyOTP (@RequestBody OTPVerifyRequest request) {
        otpService.verifyOTP(request);
        return ResponseEntity.ok(ResponseObject.builder()
        .code("VERIFY_SUCCESS")
        .content(request)
        .message("Verify OTP Success")
        .isSuccess(true)
        .requestTime(LocalDateTime.now())
        .build());
    }

    @PatchMapping(path = "verify/business")
    public ResponseEntity<ResponseObject> verifyBusinessAccount(@RequestParam(name = "otp") String otp) {
        var identity = otpService.verifyOTP(otp);
        var result = businessService.verifyBusinessAccount(identity);
        return ResponseEntity.ok(
            ResponseObject.builder()
            .code("VERIFY_SUCCESS")
            .content(result)
            .message("Verify OTP Success")
            .isSuccess(true)
            .requestTime(LocalDateTime.now())
            .build()
        );
    }
}
