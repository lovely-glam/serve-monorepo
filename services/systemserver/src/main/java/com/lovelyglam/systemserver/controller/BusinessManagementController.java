package com.lovelyglam.systemserver.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.database.model.dto.response.ResponseObject;
import com.lovelyglam.systemserver.service.BusinessManagerService;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("business-management")
public class BusinessManagementController {
    private final BusinessManagerService businessManagerService;

    @GetMapping()
    public ResponseEntity<ResponseObject> getServices(
            @PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable,
            @RequestParam(name = "q", required = false) String query) {
        var queryDto = SearchRequestParamsDto.builder()
                .search(query)
                .wrapSort(pageable)
                .build();
        var result = businessManagerService.getBusinessProfile(queryDto);
        return ResponseEntity.ok(ResponseObject.builder()
                .code("GET_BUSINESS_SUCCESS")
                .content(result)
                .message("Get Business Success")
                .isSuccess(true)
                .status(HttpStatus.OK)
                .requestTime(LocalDateTime.now())
                .build());
    }

    @GetMapping("get-all-booking-payments")
    public ResponseEntity<ResponseObject> getBookingPayments(
            @Schema
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(name = "q", required = false) String query){
        var searchQuery = SearchRequestParamsDto.builder()
                .search(query)
                .pageable(pageable)
                .build();
        var result = businessManagerService.getBookingPayments(searchQuery);
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

    @GetMapping("get-all-booking-payments/{shopId}")
    public ResponseEntity<ResponseObject> getBookingPaymentsByShop(
            @PathVariable BigDecimal shopId,
            @Schema
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(name = "q", required = false) String query){
        var searchQuery = SearchRequestParamsDto.builder()
                .search(query)
                .pageable(pageable)
                .build();
        var result = businessManagerService.getBookingPaymentsByShopId(searchQuery, shopId);
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

    @GetMapping("get-completed-payment")
    public ResponseEntity<ResponseObject> getPaymentSummary() {

            Map<String, Object> summary = businessManagerService.getCompletedPayment();
            return ResponseEntity.ok(ResponseObject.builder()
                    .code("GET_PAYMENT_SUMMARY_SUCCESS")
                    .content(summary)
                    .message("Get Payment Summary Success")
                    .isSuccess(true)
                    .status(HttpStatus.OK)
                    .requestTime(LocalDateTime.now())
                    .build());

    }
}
