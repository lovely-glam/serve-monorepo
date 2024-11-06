package com.lovelyglam.nailserver.controller;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lovelyglam.database.model.constant.AppointmentStatus;
import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.database.model.dto.response.BookingResponse;
import com.lovelyglam.database.model.dto.response.ResponseObject;
import com.lovelyglam.nailserver.service.BookingService;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "bookings")
public class BookingController {
    private final BookingService bookingService;
    @GetMapping
    public ResponseEntity<ResponseObject> getAllBookingsByUser(
            @Schema
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(name = "q", required = false) String query){
        var searchQuery = SearchRequestParamsDto.builder()
                .search(query)
                .pageable(pageable)
                .build();
        var result = bookingService.getBookingsByShopId(searchQuery);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .code("GET_BOOKINGS_SUCCESS")
                        .content(result)
                        .requestTime(LocalDateTime.now())
                        .status(HttpStatus.OK)
                        .isSuccess(true)
                        .message("Query Success")
                        .build()
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseObject> changeBookingStatus(AppointmentStatus status,@PathVariable BigDecimal id) {
        var result = bookingService.updateBookingStatus(status, id);
        return ResponseEntity.ok(
                ResponseObject.builder()
                .code("UPDATE_BOOKING_STATUS_SUCCESS")
                .content(result)
                .status(HttpStatus.OK)
                .requestTime(LocalDateTime.now())
                .isSuccess(true)
                .message("Modify Success")
                .build()
        );
    }
    @GetMapping("get-by-start-time")
    public ResponseEntity<ResponseObject> getAllBookingsByStartTime(
            @RequestParam(name = "startTime") Timestamp startTime) {
        Collection<BookingResponse> bookings = bookingService.getBookingsByStartTimeAndShopId(startTime);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .code("GET_BOOKINGS_SUCCESS")
                        .content(bookings)
                        .status(HttpStatus.OK)
                        .isSuccess(true)
                        .requestTime(LocalDateTime.now())
                        .message("Query Success")
                        .build()
        );
    }
    @GetMapping("get-by-making-date")
    public ResponseEntity<ResponseObject> getAllBookingsByStartTime(
            @RequestParam(name = "makingDate") Date makingDate) {
        Collection<BookingResponse> bookings = bookingService.getBookingsByDayAndShopId(makingDate);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .code("GET_BOOKINGS_SUCCESS")
                        .content(bookings)
                        .status(HttpStatus.OK)
                        .requestTime(LocalDateTime.now())
                        .isSuccess(true)
                        .message("Query Success")
                        .build()
        );
    }
    public ResponseEntity<ResponseObject> getBookingById (BigDecimal id) {
        var response = bookingService.getBookingById(id);
        return ResponseEntity.ok(
                ResponseObject.builder()
                .code("GET_BOOKING_SUCCESS")
                .content(response)
                .status(HttpStatus.OK)
                .requestTime(LocalDateTime.now())
                .isSuccess(true)
                .message("Query Success")
                .build()
        );
    }
}
