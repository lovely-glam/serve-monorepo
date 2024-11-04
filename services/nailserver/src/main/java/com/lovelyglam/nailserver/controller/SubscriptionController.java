package com.lovelyglam.nailserver.controller;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lovelyglam.database.model.constant.MerchantType;
import com.lovelyglam.database.model.dto.request.SubscriptionOrderRequest;
import com.lovelyglam.database.model.dto.response.ResponseObject;
import com.lovelyglam.database.model.exception.ActionFailedException;
import com.lovelyglam.database.model.exception.ValidationFailedException;
import com.lovelyglam.nailserver.service.SubscriptionPlanService;
import com.lovelyglam.utils.payment.service.PayOsService;
import com.lovelyglam.utils.payment.service.VNPayService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("payments")
public class SubscriptionController {
    private final VNPayService vnPayService;
    private final SubscriptionPlanService subscriptionPlanService;
    private final PayOsService payOsService;

    @PostMapping("/subscription")
    @Transactional(rollbackFor = {ActionFailedException.class, ValidationFailedException.class})
    public ResponseEntity<ResponseObject> submitOrder(
        @RequestBody SubscriptionOrderRequest subscriptionOrderRequest,
        HttpServletRequest request
        ) {
        String url = subscriptionPlanService.signSubscriptionPlan((paymentRequest) -> {
            MerchantType type = subscriptionOrderRequest.getType();
            
            paymentRequest.setCallBackUrl(subscriptionOrderRequest.getCallbackUrl());
            switch (type) {
                case VN_PAY:
                    paymentRequest.setMerchantCallBackUrl("/api/v1/payments/vnpay-payment");
                    return vnPayService.createOrder(paymentRequest, request);
                case PAY_OS:
                    return payOsService.createOrder(
                        paymentRequest.getTotalAmount(), 
                        paymentRequest.getOrderInfo(), 
                        "/api/v1/payments/payos/success", 
                        "/api/v1/payments/payos/failed", 
                        paymentRequest.getOrderId(), 
                        request
                    );
                default:
                    throw new ValidationFailedException("The method payment are not supported");
            }
        }, subscriptionOrderRequest);
        Map<String, String> response = new HashMap<>();
        response.put("paymentUrl", url);
        return ResponseEntity.ok(
            ResponseObject.builder()
            .code("PAYMENT_RETURN")
            .message("Payment Service Return")
            .isSuccess(true)
            .status(HttpStatus.OK)
            .content(response)
            .requestTime(LocalDateTime.now())
        .build()
        );
    }

    @GetMapping("/vnpay-payment")
    public ResponseEntity<ResponseObject> getPaymentStatus(HttpServletRequest request) {
        var result = vnPayService.convertVNPayCallbackInfo(request);
        var returnResult = subscriptionPlanService.activeSubscription(result);
        return ResponseEntity.status(HttpStatus.FOUND)
        .location(URI.create(String.format("%s?status=%s", returnResult.getCallbackUrl(), returnResult.isStatus() ? "success": "failed")))
        .body(ResponseObject.builder()
            .code("PAYMENT_RETURN")
            .message("Payment Service Return")
            .isSuccess(true)
            .status(HttpStatus.FOUND)
            .content(result)
            .requestTime(LocalDateTime.now())
        .build());
    }
    @GetMapping("/payos/success")
    public ResponseEntity<ResponseObject> payOSSuccessCallback(HttpServletRequest request) {
        var callbackResult = payOsService.convertPayOSCallbackInfo(request);
        var result = subscriptionPlanService.activeSubscription(callbackResult);
        return ResponseEntity.status(HttpStatus.FOUND)
        .location(URI.create(String.format("%s?status=%s", result.getCallbackUrl(), result.isStatus() ? "success": "failed")))
        .body(ResponseObject.builder()
            .code("PAYMENT_RETURN")
            .message("Payment Service Return")
            .isSuccess(true)
            .status(HttpStatus.FOUND)
            .content(result)
            .requestTime(LocalDateTime.now())
        .build());
    }

    @GetMapping("/payos/failed")
    public ResponseEntity<ResponseObject> payOSFailedCallback(HttpServletRequest request) {
        var callbackResult = payOsService.convertPayOSCallbackInfo(request);
        var result = subscriptionPlanService.activeSubscription(callbackResult);
        return ResponseEntity.status(HttpStatus.FOUND)
        .location(URI.create(String.format("%s?status=%s", result.getCallbackUrl(), result.isStatus() ? "success": "failed")))
        .body(ResponseObject.builder()
            .code("PAYMENT_RETURN")
            .message("Payment Service Return")
            .isSuccess(true)
            .status(HttpStatus.FOUND)
            .content(result)
            .requestTime(LocalDateTime.now())
        .build());
    }
}
