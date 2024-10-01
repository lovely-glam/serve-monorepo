package com.lovelyglam.authserver.service;

import com.lovelyglam.database.model.dto.request.OAuth2AuthenticationRequest;
import com.lovelyglam.database.model.dto.request.OAuthAuthenticationRequest;

public interface OAuth2TokenVerifierService {
    OAuth2AuthenticationRequest verifyToken(OAuthAuthenticationRequest request);
}
