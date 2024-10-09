package com.lovelyglam.nailserver.controller;

import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.database.model.dto.response.ResponseObject;
import com.lovelyglam.nailserver.service.BookingService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "booking")
public class BookingController {
    private final BookingService bookingService;
    @GetMapping("get-by-shop-id")
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
                        .status(HttpStatus.OK)
                        .isSuccess(true)
                        .message("Query Success")
                        .build()
        );
    }
}
