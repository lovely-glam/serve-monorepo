package com.lovelyglam.chatsocketserver.service.impl;

import java.sql.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.lovelyglam.chatsocketserver.config.JwtTokenConfig;
import com.lovelyglam.chatsocketserver.service.JwtService;
import com.lovelyglam.database.model.constant.TokenType;
import com.lovelyglam.database.model.other.UserClaims;

import io.jsonwebtoken.Claims;
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
        var roles = user.getAuthorities().toArray(new GrantedAuthority[0]);
        var role = roles[0];
        return generateToken(user.getUsername(),role.getAuthority(),tokenType);
    }

    public Claims generateClaims (UserClaims claimInfo) {
        Claims claims = Jwts.claims();
        claims.put("user", claimInfo);
        return claims;
    }
    public String generateToken(String username, String role, TokenType tokenType) {
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
                .setClaims(generateClaims(UserClaims.builder()
                .username(username)
                .role(role)
                .tokenType(tokenType)
                .build()))
                .setExpiration(expiryDate)
                .signWith(getSigningKey(tokenType))
                .compact();
    }

    public String generateToken(UserClaims userClaims) {
        return generateToken(userClaims.getUsername(),userClaims.getRole(),userClaims.getTokenType());
    }

    public String generateAccessToken(Authentication authentication) {
        return generateToken(authentication, TokenType.ACCESS_TOKEN);
    }

    public String generateRefreshToken(Authentication authentication) {
        return generateToken(authentication, TokenType.REFRESH_TOKEN);
    }
    private SecretKey getSigningKey(TokenType tokenType) {
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

    public UserClaims getUserClaimsFromJwt(String token, TokenType tokenType) {
        var claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey(tokenType))
                .build()
                .parseClaimsJws(token).getBody();
        @SuppressWarnings("unchecked")
        Map<String, Object> userClaimsMap = (Map<String, Object>) claims.get("user");
        return convertMapToUserClaims(userClaimsMap);
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

    private UserClaims convertMapToUserClaims(Map<String, Object> map) {
        String username = (String) map.get("username");
        String role = (String) map.get("role");
        TokenType tokenType = TokenType.valueOf((String) map.get("tokenType"));
    
        return UserClaims.builder()
                .username(username)
                .role(role)
                .tokenType(tokenType)
                .build();
    }
    
}
