package com.lovelyglam.systemserver.service;

import java.math.BigDecimal;

import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.database.model.dto.response.PaginationResponse;
import com.lovelyglam.database.model.dto.response.ShopAccountResponse;

public interface NailAccountService {
    ShopAccountResponse disableShopAccount(BigDecimal id);
    ShopAccountResponse activeShopAccount(BigDecimal id);
    ShopAccountResponse getShopAccount(BigDecimal id);
    PaginationResponse<ShopAccountResponse> getShopAccounts (SearchRequestParamsDto request);
}
