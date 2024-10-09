package com.lovelyglam.nailserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.lovelyglam.database.model.dto.request.SubscriptionOrderRequest;
import com.lovelyglam.database.model.dto.response.ResponseObject;
import com.lovelyglam.nailserver.service.SubscriptionPlanService;
import com.lovelyglam.utils.payment.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("payments")
public class SubscriptionController {
    private final VNPayService vnPayService;
    private final SubscriptionPlanService subscriptionPlanService;

    @PostMapping("/subscription")
    public ResponseEntity<ResponseObject> submitOrder(
        SubscriptionOrderRequest subscriptionOrderRequest,
        HttpServletRequest request
        ) {
        String url = subscriptionPlanService.signSubscriptionPlan((paymentRequest) -> {

            paymentRequest.setMerchantCallBackUrl("/api/v1/payments/subscription");
            paymentRequest.setCallBackUrl(subscriptionOrderRequest.getCallbackUrl());
            
            return vnPayService.createOrder(paymentRequest, request);
        }, subscriptionOrderRequest);
        Map<String, String> response = new HashMap<>();
        response.put("paymentUrl", url);
        
        return ResponseEntity.ok(
            ResponseObject.builder()
            .code("PAYMENT_RETURN")
            .message("Payment Service Return")
            .isSuccess(true)
            .status(HttpStatus.FOUND)
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
        .location(URI.create(returnResult.getCallbackUrl() + "/payments"))
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
