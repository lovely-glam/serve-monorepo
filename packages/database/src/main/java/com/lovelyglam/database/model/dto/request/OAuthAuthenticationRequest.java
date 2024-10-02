package com.lovelyglam.database.model.dto.request;

import com.lovelyglam.database.model.constant.LoginMethodType;

public record OAuthAuthenticationRequest (String authToken, LoginMethodType socialMethod) {
    
}
