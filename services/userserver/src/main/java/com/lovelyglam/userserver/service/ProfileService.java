package com.lovelyglam.userserver.service;

import com.lovelyglam.database.model.dto.request.UserAccountRequest;
import com.lovelyglam.database.model.dto.response.ProfileResponse;

import java.math.BigDecimal;

public interface ProfileService {
    ProfileResponse getMe();
    ProfileResponse updateProfile(UserAccountRequest userAccountRequest);
    ProfileResponse changePassword(String password, String rePassword);
}
