package com.lovelyglam.nailserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.lovelyglam.database.model.dto.response.ResponseObject;
import com.lovelyglam.utils.payment.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("payments")
public class PaymentFeeController {
    private final VNPayService vnPayService;

    @PostMapping("/submitOrder")
    public ResponseEntity<Map<String, String>> submitOrder(@RequestParam("amount") int orderTotal,
                                                           @RequestParam("orderInfo") String orderInfo,
                                                           HttpServletRequest request
                                                           ) {
        String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, "/api/v1/payments/vnpay-payment", request);

        Map<String, String> response = new HashMap<>();
        response.put("paymentUrl", vnpayUrl);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vnpay-payment")
    public ResponseEntity<ResponseObject> getPaymentStatus(HttpServletRequest request) {
        var result = vnPayService.convertVNPayCallbackInfo(request);

        return ResponseEntity.ok(ResponseObject.builder()
            .code("PAYMENT_RETURN")
            .message("Payment Service Return")
            .isSuccess(true)
            .content(result)
            .requestTime(LocalDateTime.now())
        .build());
    }
}
