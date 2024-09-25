package com.lovelyglam.database.model.dto.request;

import lombok.Data;

@Data
public class LocalAuthenticationRequest {
    private String username;
    private String password;
}
