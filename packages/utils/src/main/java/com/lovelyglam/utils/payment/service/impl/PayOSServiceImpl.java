package com.lovelyglam.utils.payment.service.impl;

import org.springframework.stereotype.Service;

import com.lovelyglam.utils.payment.model.PayOSAPICallback;
import com.lovelyglam.utils.payment.service.PayOsService;
import com.lovelyglam.utils.payment.util.VNPayUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import vn.payos.PayOS;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;
import vn.payos.type.PaymentLinkData;

@Service
@RequiredArgsConstructor
public class PayOSServiceImpl implements PayOsService {
    private final VNPayUtils utils;
    private final PayOS payOS;

    @Override
    public String createOrder(int totalAmount, String orderInfo, String returnUri, String failedUri, int orderId,
            HttpServletRequest request) {
        ItemData orderData = ItemData.builder()
                .name(orderInfo)
                .price(totalAmount)
                .quantity(1)
                .build();
        PaymentData paymentData = PaymentData.builder()
                .orderCode((long)orderId)
                .amount(totalAmount).item(orderData)
                .description("Payment LovelyGlam")
                .returnUrl(utils.generateCallbackUrl(request, returnUri))
                .cancelUrl(utils.generateCallbackUrl(request, failedUri))
                .build();
        try {
            return payOS.createPaymentLink(paymentData).getCheckoutUrl();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public PayOSAPICallback convertPayOSCallbackInfo(HttpServletRequest request) {
        int orderCode = Integer.parseInt(request.getParameter("orderCode"));
        try {
            PaymentLinkData data = payOS.getPaymentLinkInformation((long) orderCode);
            return PayOSAPICallback.builder()
                    .orderId(request.getParameter("orderCode"))
                    .paymentStatus("PAID".equals(data.getStatus()))
                    .orderInfo(data.getStatus())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return PayOSAPICallback.builder()
                    .orderId(request.getParameter("orderCode"))
                    .paymentStatus(false)
                    .build();
        }
    }

}
