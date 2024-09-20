package com.lovelyglam.userserver.service.impl;

import org.springframework.stereotype.Service;

import com.lovelyglam.database.model.dto.response.ProfileResponse;
import com.lovelyglam.database.repository.UserAccountRepository;
import com.lovelyglam.userserver.service.ProfileService;
import com.lovelyglam.userserver.util.AuthUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final UserAccountRepository userAccountRepository;
    private final AuthUtils authUtils;
    @Override
    public ProfileResponse getMe() {
        var userAccount = authUtils.getUserAccountFromAuthentication();
        return ProfileResponse.builder()
        .avatarUrl(userAccount.getAvatarUrl())
        .fullName(userAccount.getFullname())
        .email(userAccount.getAvatarUrl())
        .build();
    }
}
