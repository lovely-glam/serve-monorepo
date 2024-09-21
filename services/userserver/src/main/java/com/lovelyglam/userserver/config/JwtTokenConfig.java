package com.lovelyglam.userserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class JwtTokenConfig {
    @Value("${access-token.secret-key}")
    private String jwtSecret;

    @Value("${access-token.max-age}")
    private long jwtExpiration;

    @Value("${refresh-token.secret-key}")
    private String jwtRefreshSecret;

    @Value("${refresh-token.max-age}")
    private long jwtRefreshExpiration;
}
