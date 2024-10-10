package com.lovelyglam.utils.payment.service;

import com.lovelyglam.utils.payment.model.PaymentAPIRequest;
import com.lovelyglam.utils.payment.model.VNPayApiCallback;

import jakarta.servlet.http.HttpServletRequest;

public interface VNPayService {
    String createOrder(int total, String orderInfo, String urlReturn, HttpServletRequest request);
    int orderReturn(HttpServletRequest request);
    VNPayApiCallback convertVNPayCallbackInfo(HttpServletRequest request);
    String createOrder(PaymentAPIRequest paymentRequest, HttpServletRequest request);
    String createOrder(int totalAmount, String orderInfo, String returnUri, String orderId, HttpServletRequest request);
}
