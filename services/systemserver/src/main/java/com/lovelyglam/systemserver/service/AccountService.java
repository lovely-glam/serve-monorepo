package com.lovelyglam.systemserver.service;

import java.math.BigDecimal;

import com.lovelyglam.database.model.dto.response.ProfileResponse;
import com.lovelyglam.database.model.dto.response.ShopAccountResponse;

public interface AccountService {
    ProfileResponse disableUserAccount(BigDecimal id);
    ProfileResponse activeUserAccount(BigDecimal id);
    ShopAccountResponse disableShopAccount(BigDecimal id);
    ShopAccountResponse activeShopAccount(BigDecimal id);
    ProfileResponse getUserAccount(BigDecimal id);
    ShopAccountResponse getShopAccount(BigDecimal id);
}
