package com.lovelyglam.userserver.service;

import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.database.model.dto.response.PaginationResponse;
import com.lovelyglam.database.model.dto.response.ShopProfileOutstandingResponse;
import com.lovelyglam.database.model.dto.response.ShopProfileResponse;

import java.math.BigDecimal;
import java.util.List;

public interface ShopProfileService {
    PaginationResponse<ShopProfileResponse> getShops(SearchRequestParamsDto request);
    ShopProfileResponse getShopDetailById(BigDecimal id);
    List<ShopProfileOutstandingResponse> getShopProfileOutstanding();
}
