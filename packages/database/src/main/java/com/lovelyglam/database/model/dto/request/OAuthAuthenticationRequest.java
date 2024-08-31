package com.lovelyglam.database.model.dto.request;

import com.lovelyglam.database.model.constant.LoginMethodType;

public record OAuthAuthenticationRequest (String oauthId, String oauthEmail, LoginMethodType loginType) {
    
}
