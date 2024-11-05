package com.lovelyglam.utils.payment.service;

import com.lovelyglam.utils.payment.model.PayOSAPICallback;

import jakarta.servlet.http.HttpServletRequest;

public interface PayOsService {
    PayOSAPICallback convertPayOSCallbackInfo(HttpServletRequest request);
    String createOrder(int totalAmount, String orderInfo, String returnUri, String failedUri, int orderId, HttpServletRequest request);
}
