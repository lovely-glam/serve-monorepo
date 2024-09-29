package com.lovelyglam.database.model.dto.request;

import com.lovelyglam.database.model.constant.LoginMethodType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OAuth2AuthenticationRequest {
    String externalId;
    String externalEmail;
    LoginMethodType socialMethod;
    String avatar;
}
