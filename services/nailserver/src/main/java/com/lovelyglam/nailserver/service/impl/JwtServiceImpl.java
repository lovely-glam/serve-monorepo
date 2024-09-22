package com.lovelyglam.nailserver.service.impl;

import java.security.Key;
import java.sql.Date;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.lovelyglam.nailserver.config.JwtTokenConfig;
import com.lovelyglam.nailserver.service.JwtService;
import com.lovelyglam.database.model.constant.TokenType;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final JwtTokenConfig jwtTokenConfig;
    public String generateToken(Authentication authentication, TokenType tokenType) {
        UserDetails user = (UserDetails) authentication.getPrincipal();
        return generateToken(user.getUsername(), tokenType);
    }

    public String generateToken(String username, TokenType tokenType) {
        Date currentDate = new Date(System.currentTimeMillis());
        Date expiryDate = null;
        if(tokenType == TokenType.ACCESS_TOKEN) {
            expiryDate = new Date(currentDate.getTime() + jwtTokenConfig.getJwtExpiration());
        }else if (tokenType == TokenType.REFRESH_TOKEN) {
            expiryDate = new Date(currentDate.getTime() + jwtTokenConfig.getJwtRefreshExpiration());
        }
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(tokenType))
                .compact();
    }

    public String generateAccessToken(Authentication authentication) {
        return generateToken(authentication, TokenType.ACCESS_TOKEN);
    }

    public String generateRefreshToken(Authentication authentication) {
        return generateToken(authentication, TokenType.REFRESH_TOKEN);
    }
    private Key getSigningKey(TokenType tokenType) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(tokenType == TokenType.ACCESS_TOKEN ? jwtTokenConfig.getJwtSecret() : jwtTokenConfig.getJwtRefreshSecret()));
    }
    public String getUsernameFromJWT(String token, TokenType tokenType) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(tokenType))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    public boolean validateToken(String authToken, TokenType tokenType) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey(tokenType)).build().parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
