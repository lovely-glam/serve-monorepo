package com.lovelyglam.userserver.service;

import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.database.model.dto.response.PaginationResponse;
import com.lovelyglam.database.model.dto.response.ShopProfileResponse;

import java.math.BigDecimal;

public interface ShopProfileService {
    PaginationResponse<ShopProfileResponse> getShops(SearchRequestParamsDto request);
    ShopProfileResponse getShopDetailById(BigDecimal id);

}
