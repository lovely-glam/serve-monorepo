package com.lovelyglam.systemserver.service;

import java.math.BigDecimal;

import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.database.model.dto.response.PaginationResponse;
import com.lovelyglam.database.model.dto.response.ProfileResponse;
import com.lovelyglam.database.model.dto.response.ShopAccountResponse;

public interface AccountService {
    ProfileResponse disableUserAccount(BigDecimal id);
    ProfileResponse activeUserAccount(BigDecimal id);
    ProfileResponse getUserAccount(BigDecimal id);
    PaginationResponse<ProfileResponse> getUserAccounts (SearchRequestParamsDto request);
}
