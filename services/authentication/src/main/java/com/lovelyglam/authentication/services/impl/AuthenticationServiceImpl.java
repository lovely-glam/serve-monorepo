package com.lovelyglam.authentication.services.impl;

import org.springframework.stereotype.Service;

import com.lovelyglam.authentication.services.AuthenticationService;
import com.lovelyglam.database.model.dto.request.LocalAuthenticationRequest;
import com.lovelyglam.database.model.dto.response.AuthenticationResponse;
import com.lovelyglam.database.model.dto.response.ResponseObject;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {@Override
    public ResponseObject<AuthenticationResponse> localAuthentication(LocalAuthenticationRequest request) {
        return null;
    }
    
}
