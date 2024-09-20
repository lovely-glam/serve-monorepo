package com.lovelyglam.userserver.service;

import com.lovelyglam.database.model.dto.response.ProfileResponse;

public interface ProfileService {
    ProfileResponse getMe();
}
