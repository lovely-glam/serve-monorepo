package com.lovelyglam.authserver.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lovelyglam.authserver.service.BusinessService;
import com.lovelyglam.authserver.service.CustomerAccountService;
import com.lovelyglam.authserver.service.OTPService;
import com.lovelyglam.database.model.dto.request.OTPRequest;
import com.lovelyglam.database.model.dto.request.OTPVerifyRequest;
import com.lovelyglam.database.model.dto.response.ResponseObject;
import com.lovelyglam.database.model.exception.ValidationFailedException;
import com.lovelyglam.database.model.other.OTPKey;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "otp")
@RequiredArgsConstructor
public class OTPVerifyController {
    private final OTPService otpService;
    private final BusinessService businessService;
    private final CustomerAccountService customerAccountService;

    @PatchMapping(path = "verify-active")
    public ResponseEntity<ResponseObject> verifyBusinessAccount(@RequestBody OTPVerifyRequest request) {
        otpService.verifyOTP(request);
        switch (request.getType()) {
            case ACTIVE_BUSINESS_ACCOUNT:
                var resultBusiness = businessService.verifyBusinessAccount(request.getIdentity());
                return ResponseEntity.ok(
                        ResponseObject.builder()
                                .code("VERIFY_SUCCESS")
                                .content(resultBusiness)
                                .message("Verify OTP Success")
                                .isSuccess(true)
                                .status(HttpStatus.OK)
                                .requestTime(LocalDateTime.now())
                                .build());
            default:
                throw new ValidationFailedException("Not Support Method");
        }

    }

    @PatchMapping(path = "reverify-otp")
    public ResponseEntity<ResponseObject> resendOTPAccount(@RequestBody OTPRequest request) {
        switch (request.getType()) {
            case ACTIVE_BUSINESS_ACCOUNT:
                var verify = businessService.isIdentityExisted(request.getIdentity());
                otpService.generateOTPCode(OTPKey.builder()
                        .identity(request.getIdentity())
                        .otpType(request.getType())
                        .build(), verify.getUsername(), "");
                return ResponseEntity.ok(
                        ResponseObject.builder()
                                .code("VERIFY_SUCCESS")
                                .content("Check email for OTP")
                                .message("Verify OTP Success")
                                .isSuccess(true)
                                .status(HttpStatus.OK)
                                .requestTime(LocalDateTime.now())
                                .build());
            default:
                throw new ValidationFailedException("Not Support Method");
        }
    }
}
