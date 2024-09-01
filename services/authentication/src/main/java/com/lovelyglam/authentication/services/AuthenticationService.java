package com.lovelyglam.authentication.services;

import com.lovelyglam.database.model.dto.request.LocalAuthenticationRequest;
import com.lovelyglam.database.model.dto.response.AuthenticationResponse;
import com.lovelyglam.database.model.dto.response.ResponseObject;

public interface AuthenticationService {
    public ResponseObject<AuthenticationResponse> localAuthentication(LocalAuthenticationRequest request);
}
