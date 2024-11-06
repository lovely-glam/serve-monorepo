package com.lovelyglam.nailserver.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;

import com.lovelyglam.database.model.constant.AppointmentStatus;
import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.database.model.dto.response.BookingResponse;
import com.lovelyglam.database.model.dto.response.PaginationResponse;

public interface BookingService {
    PaginationResponse<BookingResponse> getBookingsByShopId(SearchRequestParamsDto request);
    Collection<BookingResponse> getBookingsByDayAndShopId(Date makingDate);
    Collection<BookingResponse> getBookingsByStartTimeAndShopId(Timestamp startTime);
    BookingResponse getBookingById(BigDecimal id);
    BookingResponse updateBookingStatus(AppointmentStatus status, BigDecimal id);
}
