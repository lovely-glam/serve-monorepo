package com.lovelyglam.authserver.service.impl;

import org.springframework.stereotype.Service;

import com.lovelyglam.authserver.service.AuthService;
import com.lovelyglam.authserver.service.JwtService;
import com.lovelyglam.database.model.dto.request.LocalAuthenticationRequest;
import com.lovelyglam.database.model.dto.response.AuthenticationResponse;
import com.lovelyglam.database.model.exception.AuthenticationErrorException;
import com.lovelyglam.database.repository.UserAccountRepository;

import java.math.BigDecimal;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserAccountRepository userAccountRepository;
    
    public AuthenticationResponse localAuthentication (LocalAuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtService.generateAccessToken(authentication);
        String refreshToken = jwtService.generateRefreshToken(authentication);
        return AuthenticationResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken).build();
    }

    public AuthenticationResponse oauthAuthentication () {
        OAuth2User user = (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BigDecimal userID = (BigDecimal) user.getAttribute("userId");
        if (userID == null) throw new AuthenticationErrorException();
        userAccountRepository.findById(userID).orElseThrow(() ->  new AuthenticationErrorException());
        return null;
    }
}
