package com.lovelyglam.systemserver.service;

import com.lovelyglam.database.model.dto.response.ProfileResponse;
import com.lovelyglam.database.model.dto.response.ShopAccountResponse;
import com.lovelyglam.database.model.dto.response.ShopProfileResponse;

import java.math.BigDecimal;

public interface AccountService {
    ProfileResponse disableUserAccount(BigDecimal id);
    ProfileResponse activeUserAccount(BigDecimal id);
    ShopAccountResponse disableShopAccount(BigDecimal id);
    ShopAccountResponse activeShopAccount(BigDecimal id);

}
