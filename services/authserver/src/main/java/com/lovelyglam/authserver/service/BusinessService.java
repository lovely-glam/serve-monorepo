package com.lovelyglam.authserver.service;

import com.lovelyglam.database.model.dto.request.BusinessRegisterRequest;
import com.lovelyglam.database.model.dto.response.BusinessRegisterResponse;

public interface BusinessService {
    BusinessRegisterResponse registerCustomerAccount(BusinessRegisterRequest request);
}
