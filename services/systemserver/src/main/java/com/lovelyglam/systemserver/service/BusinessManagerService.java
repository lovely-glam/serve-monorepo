package com.lovelyglam.systemserver.service;

import java.math.BigDecimal;
import java.util.Map;

import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.database.model.dto.response.BookingPaymentResponse;
import com.lovelyglam.database.model.dto.response.NailProfileManagerResponse;
import com.lovelyglam.database.model.dto.response.PaginationResponse;

public interface BusinessManagerService {
    PaginationResponse<NailProfileManagerResponse> getBusinessProfile (SearchRequestParamsDto request);
    PaginationResponse<BookingPaymentResponse> getBookingPayments (SearchRequestParamsDto request);
    PaginationResponse<BookingPaymentResponse> getBookingPaymentsByShopId (SearchRequestParamsDto request, BigDecimal shopId);
    public Map<String, Object> getCompletedPayment();
}
