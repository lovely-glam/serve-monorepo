package com.lovelyglam.authserver.service.impl;

import org.springframework.stereotype.Service;

import com.lovelyglam.authserver.service.AuthService;
import com.lovelyglam.authserver.service.JwtService;
import com.lovelyglam.database.model.constant.TokenType;
import com.lovelyglam.database.model.dto.request.LocalAuthenticationRequest;
import com.lovelyglam.database.model.dto.response.AuthenticationResponse;
import com.lovelyglam.database.model.entity.UserAccount;
import com.lovelyglam.database.model.exception.AuthFailedException;
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
    
    @Override
    public AuthenticationResponse localAuthentication (LocalAuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtService.generateAccessToken(authentication);
        String refreshToken = jwtService.generateRefreshToken(authentication);
        return AuthenticationResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken).build();
    }

    @Override
    public AuthenticationResponse oauthAuthentication () {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User user = (OAuth2User) authentication.getPrincipal();
            BigDecimal userID = (BigDecimal) user.getAttribute("userId");
            if (userID == null) throw new AuthFailedException("Not found this user");
            UserAccount userAccount = userAccountRepository.findById(userID).orElseThrow(() ->  new AuthFailedException("Not found this account"));
            String accessToken = jwtService.generateToken(userAccount.getUsername(), TokenType.ACCESS_TOKEN);
            String refreshToken = jwtService.generateToken(userAccount.getUsername(), TokenType.REFRESH_TOKEN);
            return AuthenticationResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken).build();
        }
        throw new AuthFailedException("Not login with oauth server yet");
    }
}
