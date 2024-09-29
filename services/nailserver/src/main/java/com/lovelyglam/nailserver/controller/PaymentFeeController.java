package com.lovelyglam.nailserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.lovelyglam.utils.payment.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

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
                                                            String scheme = request.getScheme();
                                                            String serverName = request.getServerName();
                                                            int serverPort = request.getServerPort();
        String baseUrl = serverPort == 80 || serverPort == 443 
        ? scheme + "://" + serverName : scheme + "://" + serverName + ":" + serverPort;
        String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl, request);

        Map<String, String> response = new HashMap<>();
        response.put("paymentUrl", vnpayUrl);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vnpay-payment")
    public ResponseEntity<Map<String, Object>> getPaymentStatus(HttpServletRequest request) {
        int paymentStatus = vnPayService.orderReturn(request);

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        Map<String, Object> response = new HashMap<>();
        response.put("orderId", orderInfo);
        response.put("totalPrice", totalPrice);
        response.put("paymentTime", paymentTime);
        response.put("transactionId", transactionId);
        response.put("paymentStatus", paymentStatus == 1 ? "success" : "fail");

        return ResponseEntity.ok(response);
    }
}
