package com.lovelyglam.systemserver.service;

import com.lovelyglam.database.model.dto.request.UserAccountRequest;
import com.lovelyglam.database.model.dto.response.ProfileResponse;

import java.math.BigDecimal;

public interface UserAccountService {
    ProfileResponse disableAccount(BigDecimal id);
}
