package com.lovelyglam.utils.payment.service;

import com.lovelyglam.utils.payment.model.VNPayApiCallback;

import jakarta.servlet.http.HttpServletRequest;

public interface VNPayService {
    public String createOrder(int total, String orderInfo, String urlReturn, HttpServletRequest request);
    public int orderReturn(HttpServletRequest request);
    public VNPayApiCallback convertVNPayCallbackInfo(HttpServletRequest request);
}
