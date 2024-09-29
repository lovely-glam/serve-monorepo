package com.lovelyglam.authserver.service.impl;

import org.springframework.stereotype.Service;

import com.lovelyglam.authserver.service.AuthService;
import com.lovelyglam.authserver.service.JwtService;
import com.lovelyglam.database.model.constant.LoginMethodType;
import com.lovelyglam.database.model.constant.TokenType;
import com.lovelyglam.database.model.dto.request.LocalAuthenticationRequest;
import com.lovelyglam.database.model.dto.request.OAuth2AuthenticationRequest;
import com.lovelyglam.database.model.dto.response.AuthenticationResponse;
import com.lovelyglam.database.model.entity.LoginMethod;
import com.lovelyglam.database.model.entity.UserAccount;
import com.lovelyglam.database.repository.LoginMethodRepository;
import com.lovelyglam.database.repository.UserAccountRepository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserAccountRepository userAccountRepository;
    private final LoginMethodRepository loginMethodRepository;
    
    @Override
    public AuthenticationResponse localAuthentication (LocalAuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(String.format("customer_%s", request.getUsername()), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtService.generateAccessToken(authentication);
        String refreshToken = jwtService.generateRefreshToken(authentication);
        return AuthenticationResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken).build();
    }

    @Override
    public AuthenticationResponse businessAuthentication (LocalAuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(String.format("nail_%s", request.getUsername()), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtService.generateAccessToken(authentication);
        String refreshToken = jwtService.generateRefreshToken(authentication);
        return AuthenticationResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken).build();
    }

    @Override
    public AuthenticationResponse oauthAuthentication (OAuth2AuthenticationRequest request) {
        LoginMethod user = loginMethodRepository.findLoginMethodByExternalIdAndLoginMethodType(
            request.getExternalId(), request.getSocialMethod()
        ).orElseGet(() -> {
            var maxSize = userAccountRepository.count();
            var userAccountNew = UserAccount
            .builder()
            .hashPassword("")
            .build();
            var userLoginMethod = LoginMethod.builder()
            .loginType(request.getSocialMethod())
            .externalId(request.getExternalId())
            .user(userAccountNew)
            .build();
            userAccountNew.setAvatarUrl(request.getAvatar());
            if (request.getSocialMethod() == LoginMethodType.GOOGLE && request.getExternalEmail() != null) {
                
                userLoginMethod.setExternalEmail(request.getExternalEmail());
                userAccountNew.setUsername(String.format("user-google@%s",maxSize));
                userAccountNew.setFullname(String.format("user-google@%s",maxSize));
            } else {
                userLoginMethod.setExternalEmail("");
                userAccountNew.setUsername(String.format("user-facebook@%s",maxSize));
                userAccountNew.setFullname(String.format("user-facebook@%s",maxSize));
            }
            userAccountNew.setLoginMethod(List.of(userLoginMethod));
            userAccountRepository.save(userAccountNew);
            return userLoginMethod;
        });
        UserAccount userAccount = user.getUser();
        String accessToken = jwtService.generateToken(userAccount.getUsername(), "ROLE_USER", TokenType.ACCESS_TOKEN);
        String refreshToken = jwtService.generateToken(userAccount.getUsername(), "ROLE_USER", TokenType.REFRESH_TOKEN);
        return AuthenticationResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken).build();
    }
}
