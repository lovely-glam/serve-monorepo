package com.lovelyglam.userserver.controller;

import java.net.URI;
import java.time.LocalDateTime;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lovelyglam.database.model.dto.request.BookingPaymentRequest;
import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.database.model.dto.response.ResponseObject;
import com.lovelyglam.database.model.exception.ActionFailedException;
import com.lovelyglam.userserver.service.PaymentService;
import com.lovelyglam.utils.payment.service.VNPayService;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "payments")
public class PaymentController {
    private final PaymentService paymentService;
    private final VNPayService vnPayService;

    @PostMapping("/booking")
    public ResponseEntity<ResponseObject> createPaymentTransaction(@RequestBody BookingPaymentRequest bookingRequest,
            HttpServletRequest request) {
        var result = paymentService.bookingPaymentTransactionInit(bookingRequest.getBookingId(),
                bookingRequest.getCallbackUrl(), (entity) -> {
                    var url = vnPayService.createOrder(entity.getTotalPayment().intValue(), "Order Booking",
                            "/api/v1/payments/booking/callback", entity.getId().toString(), request);
                    return url;
                });
        return ResponseEntity.ok(ResponseObject.builder()
                .content(result)
                .code("PAYMENT_RETURN")
                .message("Payment Service Return")
                .isSuccess(true)
                .status(HttpStatus.OK)
                .requestTime(LocalDateTime.now())
                .build());
    }

    @GetMapping("/booking/callback")
    @Transactional(rollbackFor = { ActionFailedException.class })
    public ResponseEntity<ResponseObject> paymentTransactionCallback(HttpServletRequest request) {
        var callbackResult = vnPayService.convertVNPayCallbackInfo(request);
        var result = paymentService.bookingPaymentTransactionCallbackConfirm(callbackResult);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(result.getCallbackUrl()))
                .body(ResponseObject.builder()
                        .content(result)
                        .code("PAYMENT_RETURN")
                        .message("Payment Service Return")
                        .isSuccess(result.isStatus())
                        .status(HttpStatus.FOUND)
                        .requestTime(LocalDateTime.now())
                        .build());
    }

    @GetMapping("/histories")
    public ResponseEntity<ResponseObject> getHistoryPayment(
            @Schema @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(name = "q", required = false) String query) {
        var searchQuery = SearchRequestParamsDto.builder()
                .search(query)
                .pageable(pageable)
                .build();
        var result = paymentService.getPaymentHistories(searchQuery);
        return ResponseEntity.ok(ResponseObject.builder()
                .content(result)
                .code("PAYMENT_HISTORY_GET_SUCCESS")
                .message("Query Success")
                .isSuccess(true)
                .status(HttpStatus.OK)
                .requestTime(LocalDateTime.now())
                .build());
    }
}
