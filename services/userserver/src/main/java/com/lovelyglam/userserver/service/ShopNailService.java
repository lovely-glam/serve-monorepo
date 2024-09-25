package com.lovelyglam.userserver.service;

import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.database.model.dto.response.NailServiceResponse;
import com.lovelyglam.database.model.dto.response.PaginationResponse;
import com.lovelyglam.database.model.dto.response.ShopProfileResponse;

import java.math.BigDecimal;

public interface ShopNailService {
    PaginationResponse<NailServiceResponse> getShopNailServices(SearchRequestParamsDto request);
    NailServiceResponse getShopNailServiceById(BigDecimal id);
    PaginationResponse<NailServiceResponse> getShopNailServicesByShopId(BigDecimal shopId, SearchRequestParamsDto request);

}
