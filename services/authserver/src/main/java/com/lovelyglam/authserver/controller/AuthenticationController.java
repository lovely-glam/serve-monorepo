package com.lovelyglam.authserver.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lovelyglam.authserver.service.AuthService;
import com.lovelyglam.authserver.service.OAuth2TokenVerifierService;
import com.lovelyglam.database.model.dto.request.LocalAuthenticationRequest;
import com.lovelyglam.database.model.dto.request.OAuth2AuthenticationRequest;
import com.lovelyglam.database.model.dto.request.OAuthAuthenticationRequest;
import com.lovelyglam.database.model.dto.response.ResponseObject;

import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping(value = "auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthService authService;
    private final OAuth2TokenVerifierService oAuth2TokenVerifierService;
    @PostMapping("customer/local")
    public ResponseEntity<ResponseObject> localAuthentication(@RequestBody LocalAuthenticationRequest localAuthenticationRequest) {
        var result = authService.localAuthentication(localAuthenticationRequest);
        return ResponseEntity.ok(ResponseObject.builder()
        .code("AUTH_SUCCESS")
        .content(result)
        .message("Login Success")
        .isSuccess(true)
        .status(HttpStatus.OK)
        .requestTime(LocalDateTime.now())
        .build());
    }

    @PostMapping("nailer")
    public ResponseEntity<ResponseObject> businessAuthentication(@RequestBody LocalAuthenticationRequest localAuthenticationRequest) {
        var result = authService.businessAuthentication(localAuthenticationRequest);
        return ResponseEntity.ok(ResponseObject.builder()
        .code("AUTH_SUCCESS")
        .content(result)
        .message("Login Success")
        .isSuccess(true)
        .status(HttpStatus.OK)
        .requestTime(LocalDateTime.now())
        .build());
    }

    @PostMapping("system")
    public ResponseEntity<ResponseObject> systemAuthentication(@RequestBody LocalAuthenticationRequest localAuthenticationRequest) {
        var result = authService.systemAuthenticationResponse(localAuthenticationRequest);
        return ResponseEntity.ok(ResponseObject.builder()
        .code("AUTH_SUCCESS")
        .content(result)
        .message("Login Success")
        .isSuccess(true)
        .requestTime(LocalDateTime.now())
        .status(HttpStatus.OK)
        .build()
        );
    }

    @PostMapping("oauth2")
    public ResponseEntity<ResponseObject> oauth2Login(@RequestBody OAuth2AuthenticationRequest request) {
        var result = authService.oauthAuthentication(request);
        return ResponseEntity.ok(
            ResponseObject.builder()
            .code("OAUTH2_SUCCESS")
            .content(result)
            .message("OAuth2 Login Success")
            .isSuccess(true)
            .status(HttpStatus.OK)
            .requestTime(LocalDateTime.now())
            .build()
        );
    }

    @PostMapping("oauth2/callback")
    public ResponseEntity<ResponseObject> oauth2CallBack(@RequestBody OAuthAuthenticationRequest request) {
        var oauthInfoResult = oAuth2TokenVerifierService.verifyToken(request);
        var result = authService.oauthAuthentication(oauthInfoResult);
        return ResponseEntity.ok(
            ResponseObject.builder()
            .code("OAUTH2_SUCCESS")
            .content(result)
            .status(HttpStatus.OK)
            .message("OAuth2 Login Success")
            .isSuccess(true)
            .requestTime(LocalDateTime.now())
            .build()
        );
    }
    
    
}
