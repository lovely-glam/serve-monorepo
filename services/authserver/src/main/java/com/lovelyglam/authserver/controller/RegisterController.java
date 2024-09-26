package com.lovelyglam.authserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lovelyglam.authserver.service.BusinessService;
import com.lovelyglam.authserver.service.CustomerAccountService;
import com.lovelyglam.authserver.service.OTPService;
import com.lovelyglam.database.model.dto.request.BusinessRegisterRequest;
import com.lovelyglam.database.model.dto.request.CustomerRegisterRequest;
import com.lovelyglam.database.model.dto.response.ResponseObject;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("registers")
@RequiredArgsConstructor
public class RegisterController {    
    private final CustomerAccountService customerAccountService;
    private final BusinessService businessService;
    private final OTPService otpService;
    @PostMapping(path = "customer")
    public ResponseEntity<ResponseObject> customerRegister(@RequestBody CustomerRegisterRequest request) {
        var result = customerAccountService.registerCustomerAccount(request);
        otpService.generateOTPCode(result.email(), result.username());
        return ResponseEntity.ok(ResponseObject.builder()
            .code("CUSTOMER_REGISTER_SUCCESS")
            .content(result)
            .isSuccess(true)
            .message("Customer Register Successfully")
        .build());
    }

    @PostMapping(path = "business")
    public ResponseEntity<ResponseObject> businessRegister(@RequestBody BusinessRegisterRequest registerRequest) {
        var result = businessService.registerCustomerAccount(registerRequest);
        return ResponseEntity.ok(
            ResponseObject.builder()
            .code("BUSINESS_REGISTER_SUCCESS")
            .content(result)
            .isSuccess(true)
            .message("Business Register Successfully")
            .build()
        );
    }
}
