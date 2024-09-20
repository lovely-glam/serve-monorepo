package com.lovelyglam.authserver.controller;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lovelyglam.authserver.service.AuthService;
import com.lovelyglam.database.model.dto.request.LocalAuthenticationRequest;
import com.lovelyglam.database.model.dto.response.ResponseObject;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping(value = "auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthService authService;
    @PostMapping("local")
    public ResponseEntity<ResponseObject> localAuthentication(@RequestBody LocalAuthenticationRequest localAuthenticationRequest) {
        var result = authService.localAuthentication(localAuthenticationRequest);
        return ResponseEntity.ok(ResponseObject.builder()
        .code("AUTH_SUCCESS")
        .content(result)
        .message("Login Success")
        .isSuccess(true)
        .requestTime(LocalDateTime.now())
        .build());
    }

    @GetMapping("oauth2")
    public ResponseEntity<ResponseObject> oauth2CallBack() {
        return ResponseEntity.ok(
            ResponseObject.builder()
            .code("OAUTH2_SUCCESS")
            .content(null)
            .message("OAuth2 Login Success")
            .isSuccess(true)
            .requestTime(LocalDateTime.now())
            .build()
        );
    }

    @GetMapping("oauth2/failed")
    public ResponseEntity<ResponseObject> oauth2CallbackFailedHandler() {
        return ResponseEntity.ok(
            ResponseObject.builder()
            .code("OAUTH2_FAILED")
            .content(null)
            .message("OAuth2 Login Failed")
            .isSuccess(false)
            .requestTime(LocalDateTime.now())
            .build()
        );
    }
    
    
}
