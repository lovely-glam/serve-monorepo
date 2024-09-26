package com.lovelyglam.authserver.service;

import com.lovelyglam.database.model.dto.request.LocalAuthenticationRequest;
import com.lovelyglam.database.model.dto.response.AuthenticationResponse;

public interface AuthService {
    AuthenticationResponse localAuthentication (LocalAuthenticationRequest request);
    AuthenticationResponse oauthAuthentication ();
    AuthenticationResponse businessAuthentication (LocalAuthenticationRequest request);
}
