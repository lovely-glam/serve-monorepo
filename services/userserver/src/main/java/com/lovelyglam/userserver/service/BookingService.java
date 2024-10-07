package com.lovelyglam.userserver.service;

import com.lovelyglam.database.model.dto.request.BookingRequest;
import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.database.model.dto.response.BookingResponse;
import com.lovelyglam.database.model.dto.response.PaginationResponse;

import java.math.BigDecimal;

public interface BookingService {
    BookingResponse createBooking(BookingRequest bookingRequest);
    PaginationResponse<BookingResponse> getBookings(SearchRequestParamsDto request);
    BookingResponse getBooking(BigDecimal bookingId);
    BookingResponse updateBooking(BigDecimal bookingId, BookingRequest bookingRequest);
    BookingResponse disableBooking(BigDecimal bookingId);
    public PaginationResponse<BookingResponse> getBookingsByUserId(SearchRequestParamsDto request);
    BookingResponse acceptBooking(BigDecimal bookingId);

}
