package com.lovelyglam.userserver.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lovelyglam.database.model.dto.request.UserAccountRequest;
import com.lovelyglam.database.model.dto.response.ProfileResponse;
import com.lovelyglam.database.model.entity.UserAccount;
import com.lovelyglam.database.model.exception.ActionFailedException;
import com.lovelyglam.database.model.exception.AuthFailedException;
import com.lovelyglam.database.model.exception.NotFoundException;
import com.lovelyglam.database.repository.UserAccountRepository;
import com.lovelyglam.userserver.service.ProfileService;
import com.lovelyglam.userserver.util.AuthUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final UserAccountRepository userAccountRepository;
    private final AuthUtils authUtils;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ProfileResponse getMe() {
        var userAccount = authUtils.getUserAccountFromAuthentication();
        return ProfileResponse.builder()
        .id(userAccount.getId())
        .username(userAccount.getUsername())
        .avatarUrl(userAccount.getAvatarUrl())
        .fullName(userAccount.getFullname())
        .email(userAccount.getEmail())
        .build();
    }

    @Override
    public ProfileResponse updateProfile(UserAccountRequest userAccountRequest) {
        var userAccount = authUtils.getUserAccountFromAuthentication();
        if (userAccount == null) {
            throw new AuthFailedException("Action Require Login");
        }
        UserAccount userAccountDb = userAccountRepository.findById(userAccount.getId())
                .orElseThrow(() -> new NotFoundException("Not Found User Account"));
        userAccountDb.setEmail(userAccountRequest.getEmail());
        userAccountDb.setAvatarUrl(userAccountRequest.getAvatarUrl());
        userAccountDb.setFullname(userAccountRequest.getFullName());
        try {
            var item = userAccountRepository.save(userAccountDb);
            return ProfileResponse.builder()
                    .id(item.getId())
                    .username(item.getUsername())
                    .fullName(item.getFullname())
                    .email(item.getEmail())
                    .avatarUrl(item.getAvatarUrl())
                    .build();
        } catch (Exception ex) {
            throw new ActionFailedException(String.format("Failed update account with reason: %s", ex.getMessage()));
        }
    }

    @Override
    public ProfileResponse changePassword(String password, String rePassword) {
        var userAccount = authUtils.getUserAccountFromAuthentication();
        if (userAccount == null) {
            throw new AuthFailedException("Action Require Login");
        }
        UserAccount userAccountDb = userAccountRepository.findById(userAccount.getId())
                .orElseThrow(() -> new NotFoundException("Not Found User Account"));
        if (!password.equals(rePassword))
            throw new AuthFailedException("Password and RePassword are not matched");
        userAccountDb.setHashPassword(passwordEncoder.encode(password));
        try {
            var item = userAccountRepository.save(userAccountDb);
            return ProfileResponse.builder()
                    .id(item.getId())
                    .username(item.getUsername())
                    .fullName(item.getFullname())
                    .email(item.getEmail())
                    .avatarUrl(item.getAvatarUrl())
                    .build();
        } catch (Exception ex) {
            throw new ActionFailedException(String.format("Failed change account password with reason: %s", ex.getMessage()));
        }
    }
}
