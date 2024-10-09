package com.lovelyglam.nailserver.service;

import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.database.model.dto.response.BookingResponse;
import com.lovelyglam.database.model.dto.response.PaginationResponse;

public interface BookingService {
    PaginationResponse<BookingResponse> getBookingsByShop(SearchRequestParamsDto request);
    PaginationResponse<BookingResponse> getBookingsByTime(SearchRequestParamsDto request);
    BookingResponse getBookingById(Long id);
}
