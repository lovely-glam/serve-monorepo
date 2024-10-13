package com.lovelyglam.authserver.service;

import com.lovelyglam.database.model.dto.request.BusinessRegisterRequest;
import com.lovelyglam.database.model.dto.response.BusinessRegisterResponse;
import com.lovelyglam.database.model.dto.response.ShopAccountResponse;

public interface BusinessService {
    BusinessRegisterResponse registerBusinessAccount(BusinessRegisterRequest request);
    ShopAccountResponse verifyBusinessAccount(String businessEmail);
}
