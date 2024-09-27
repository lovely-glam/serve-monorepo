package com.lovelyglam.userserver.controller;

import com.lovelyglam.database.model.dto.request.BookingRequest;
import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.database.model.dto.response.ResponseObject;
import com.lovelyglam.userserver.service.BookingService;
import com.lovelyglam.userserver.service.ShopNailService;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "booking")
public class BookingController {
    private final BookingService bookingService;
    @GetMapping
    public ResponseEntity<ResponseObject> getAllBookings(
            @Schema
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(name = "q", required = false) String query){
        var searchQuery = SearchRequestParamsDto.builder()
                .search(query)
                .pageable(pageable)
                .build();
        var result = bookingService.getBookings(searchQuery);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .code("GET_BOOKINGS_SUCCESS")
                        .content(result)
                        .status(HttpStatus.OK)
                        .isSuccess(true)
                        .message("Query Success")
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getBookingDetail(@PathVariable(value = "id") BigDecimal id){
        var result = bookingService.getBooking(id);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .code("GET_BOOKING_DETAIL_SUCCESS")
                        .content(result)
                        .isSuccess(true)
                        .status(HttpStatus.OK)
                        .message("Query Success")
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<ResponseObject> createBooking(@RequestBody @Valid BookingRequest request, BindingResult bindingResult) {

        var result = bookingService.createBooking(request);
        var responseObject = ResponseObject.builder()
                .code("GET_BOOKING_DETAIL_SUCCESS")
                .content(result)
                .isSuccess(true)
                .status(HttpStatus.OK)
                .message("Query Success")
                .build();
        return ResponseEntity.ok().body(responseObject);
    }
}
