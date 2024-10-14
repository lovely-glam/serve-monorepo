package com.lovelyglam.chatsocketserver.service;

import org.springframework.security.core.Authentication;

import com.lovelyglam.database.model.constant.TokenType;
import com.lovelyglam.database.model.other.UserClaims;

import jakarta.servlet.http.HttpServletRequest;

public interface JwtService {
    String generateToken(Authentication authentication, TokenType tokenType);
    String generateToken(String username,String role, TokenType tokenType);
    UserClaims getUserClaimsFromJwt(String token, TokenType tokenType);
    String generateAccessToken(Authentication authentication);
    String generateRefreshToken(Authentication authentication);
    String getUsernameFromJWT(String token, TokenType tokenType);
    boolean validateToken(String authToken, TokenType tokenType);
    String getJwtFromRequest(HttpServletRequest request);
}
