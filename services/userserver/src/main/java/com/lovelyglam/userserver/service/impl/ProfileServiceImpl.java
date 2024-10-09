package com.lovelyglam.userserver.service.impl;

import com.lovelyglam.database.model.dto.request.CustomerRegisterRequest;
import com.lovelyglam.database.model.dto.request.UserAccountRequest;
import com.lovelyglam.database.model.dto.response.BookingResponse;
import com.lovelyglam.database.model.entity.Booking;
import com.lovelyglam.database.model.entity.SystemAccount;
import com.lovelyglam.database.model.entity.UserAccount;
import com.lovelyglam.database.model.exception.ActionFailedException;
import com.lovelyglam.database.model.exception.AuthFailedException;
import com.lovelyglam.database.model.exception.NotFoundException;
import org.springframework.stereotype.Service;

import com.lovelyglam.database.model.dto.response.ProfileResponse;
import com.lovelyglam.database.repository.UserAccountRepository;
import com.lovelyglam.userserver.service.ProfileService;
import com.lovelyglam.userserver.util.AuthUtils;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
        userAccountDb.setHashPassword(userAccountRequest.getPassword());
        if (!userAccountDb.getHashPassword().equals(userAccountRequest.getRePassword())) {
            throw new ActionFailedException("Password and Repassword do not match");
        }
        try {
            var item = userAccountRepository.save(userAccountDb);
            return ProfileResponse.builder()
                    .fullName(item.getFullname())
                    .email(item.getEmail())
                    .avatarUrl(item.getAvatarUrl())
                    .build();
        } catch (Exception ex) {
            throw new ActionFailedException(String.format("Failed update account with reason: %s", ex.getMessage()));
        }
    }
}
