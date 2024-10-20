package com.lovelyglam.nailserver.service;

import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.database.model.dto.response.BookingPaymentResponse;
import com.lovelyglam.database.model.dto.response.PaginationResponse;

public interface BookingPaymentService {
    PaginationResponse<BookingPaymentResponse> getBookingPaymentsByShopId (SearchRequestParamsDto request);
    PaginationResponse<BookingPaymentResponse> getAcceptedBookingPaymentsByShopId (SearchRequestParamsDto request);
}

