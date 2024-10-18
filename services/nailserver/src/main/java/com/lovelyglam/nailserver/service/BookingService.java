package com.lovelyglam.nailserver.service;

import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.database.model.dto.response.BookingResponse;
import com.lovelyglam.database.model.dto.response.PaginationResponse;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.sql.Date;

public interface BookingService {
    PaginationResponse<BookingResponse> getBookingsByShopId(SearchRequestParamsDto request);
    Collection<BookingResponse> getBookingsByDayAndShopId(Date makingDate);
    Collection<BookingResponse> getBookingsByStartTimeAndShopId(Timestamp startTime);
    BookingResponse getBookingById(Long id);
}
