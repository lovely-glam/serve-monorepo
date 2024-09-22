package com.lovelyglam.authserver.service;

import com.lovelyglam.database.model.dto.request.CustomerRegisterRequest;
import com.lovelyglam.database.model.dto.response.CustomerRegisterResponse;

public interface CustomerAccountService {
    CustomerRegisterResponse registerCustomerAccount(CustomerRegisterRequest request);
}
