package com.lovelyglam.nailserver.service;

import java.util.function.Function;

import com.lovelyglam.database.model.dto.request.SubscriptionOrderRequest;
import com.lovelyglam.database.model.dto.response.PaymentSubscriptionResponse;
import com.lovelyglam.utils.payment.model.PaymentAPIRequest;
import com.lovelyglam.utils.payment.model.VNPayApiCallback;

public interface SubscriptionPlanService {
    String signSubscriptionPlan(Function<PaymentAPIRequest, String> cb, SubscriptionOrderRequest request);
    PaymentSubscriptionResponse activeSubscription(VNPayApiCallback callback);
}
