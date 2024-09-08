package com.lovelyglam.authserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lovelyglam.authserver.service.AuthService;
import com.lovelyglam.database.model.dto.request.LocalAuthenticationRequest;
import com.lovelyglam.database.model.dto.response.ResponseObject;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthService authService;
    @PostMapping("local")
    public ResponseEntity<ResponseObject> localAuthentication(@RequestBody LocalAuthenticationRequest localAuthenticationRequest) {
        return ResponseEntity.ok(ResponseObject.builder()
        .build());
    }
}
