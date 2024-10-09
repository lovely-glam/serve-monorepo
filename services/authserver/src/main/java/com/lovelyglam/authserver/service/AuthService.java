package com.lovelyglam.authserver.service;

import com.lovelyglam.database.model.dto.request.LocalAuthenticationRequest;
import com.lovelyglam.database.model.dto.request.OAuth2AuthenticationRequest;
import com.lovelyglam.database.model.dto.response.AuthenticationResponse;

public interface AuthService {
    AuthenticationResponse localAuthentication (LocalAuthenticationRequest request);
    AuthenticationResponse oauthAuthentication (OAuth2AuthenticationRequest request);
    AuthenticationResponse businessAuthentication (LocalAuthenticationRequest request);
    AuthenticationResponse systemAuthenticationResponse (LocalAuthenticationRequest request);
}
