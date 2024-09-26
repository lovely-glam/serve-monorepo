package com.lovelyglam.database.model.other;

import com.lovelyglam.database.model.constant.TokenType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserClaims {
    private String username;
    private String role;
    private TokenType tokenType;
}
