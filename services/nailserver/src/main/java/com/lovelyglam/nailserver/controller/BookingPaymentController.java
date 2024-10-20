package com.lovelyglam.nailserver.controller;

import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.database.model.dto.response.ResponseObject;
import com.lovelyglam.nailserver.service.BookingPaymentService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "booking-payment")
public class BookingPaymentController {
    private final BookingPaymentService bookingPaymentService;

    @GetMapping("get-all-booking-payments")
    public ResponseEntity<ResponseObject> getBookingPaymentsByShop(
            @Schema
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(name = "q", required = false) String query){
        var searchQuery = SearchRequestParamsDto.builder()
                .search(query)
                .pageable(pageable)
                .build();
        var result = bookingPaymentService.getBookingPaymentsByShopId(searchQuery);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .code("GET_BOOKING_PAYMENT_SUCCESS")
                        .content(result)
                        .status(HttpStatus.OK)
                        .requestTime(LocalDateTime.now())
                        .isSuccess(true)
                        .message("Query Success")
                        .build()
        );
    }

    @GetMapping("get-all-accepted-booking-payments")
    public ResponseEntity<ResponseObject> getAcceptedBookingPaymentsByShop(
            @Schema
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(name = "q", required = false) String query){
        var searchQuery = SearchRequestParamsDto.builder()
                .search(query)
                .pageable(pageable)
                .build();
        var result = bookingPaymentService.getAcceptedBookingPaymentsByShopId(searchQuery);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .code("GET_ACCEPTED_BOOKING_PAYMENT_SUCCESS")
                        .content(result)
                        .status(HttpStatus.OK)
                        .requestTime(LocalDateTime.now())
                        .isSuccess(true)
                        .message("Query Success")
                        .build()
        );
    }
}
