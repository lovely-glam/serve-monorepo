package com.lovelyglam.nailserver.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.function.Function;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lovelyglam.database.model.constant.PaymentStatus;
import com.lovelyglam.database.model.constant.SubscriptionPlanStatus;
import com.lovelyglam.database.model.constant.SubscriptionRole;
import com.lovelyglam.database.model.dto.request.SubscriptionOrderRequest;
import com.lovelyglam.database.model.dto.response.PaymentSubscriptionResponse;
import com.lovelyglam.database.model.entity.SubscriptionPayment;
import com.lovelyglam.database.model.entity.SubscriptionPlan;
import com.lovelyglam.database.model.exception.ActionFailedException;
import com.lovelyglam.database.model.exception.ValidationFailedException;
import com.lovelyglam.database.repository.SubscriptionPaymentRepository;
import com.lovelyglam.database.repository.SubscriptionPlanRepository;
import com.lovelyglam.nailserver.service.SubscriptionPlanService;
import com.lovelyglam.nailserver.utils.AuthUtils;
import com.lovelyglam.utils.payment.model.PaymentAPIRequest;
import com.lovelyglam.utils.payment.model.VNPayApiCallback;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final SubscriptionPaymentRepository paymentRepository;
    private final AuthUtils authUtils;

    @Override
    @Transactional(rollbackFor = {ActionFailedException.class})
    public String signSubscriptionPlan(Function<PaymentAPIRequest, String> cb, SubscriptionOrderRequest request) {
        var userProfile = authUtils.getUserAccountFromAuthentication();
        var subscription = userProfile.getSubscriptionPlans();
        if (subscription == null) {
            subscription = SubscriptionPlan.builder()
                .role(request.getSubscriptionRole())
                .shopAccount(userProfile)
                .status(SubscriptionPlanStatus.PENDING)
            .build();
        }
        var currentTime = LocalDateTime.now();
            if (subscription.getExTime() != null && currentTime.isAfter(subscription.getExTime().minusWeeks(1))) {
                throw new ValidationFailedException("This subscription has been signed, please try again next month");
            }
            subscription.setStatus(SubscriptionPlanStatus.PENDING);
            try {
                var paymentEntity = createPayment(request);
                var subscriptionResult = subscriptionPlanRepository.save(subscription);
                paymentEntity.setSubscriptionPlan(subscriptionResult);
                var paymentResult = paymentRepository.save(paymentEntity);
                var paymentApiRequest = PaymentAPIRequest.builder()
                .callBackUrl(request.getCallbackUrl())
                .orderId(paymentResult.getId().intValue())
                .totalAmount(paymentResult.getTotalPayment().intValue())
                .orderInfo(String.format("PAYMENT FOR SUBSCRIPTION %s", subscription.getRole().toString()))
                .build();
                return cb.apply(paymentApiRequest);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new ActionFailedException("Failed to add subscription");
            }
    }

    public PaymentSubscriptionResponse activeSubscription(VNPayApiCallback callback) {
        BigDecimal orderId = new BigDecimal(callback.getOrderId());
        PaymentSubscriptionResponse response = new PaymentSubscriptionResponse();
        paymentRepository.findById(orderId).ifPresentOrElse((entity) -> {
            if (callback.getPaymentStatus() == true) {
                entity.setPaymentStatus(PaymentStatus.COMPLETED);
            } else {
                entity.setPaymentStatus(PaymentStatus.FAILED);
            }
            paymentRepository.save(entity);
            var subPlan = entity.getSubscriptionPlan();
            subPlan.setStatus(SubscriptionPlanStatus.ACTIVE);
            subPlan.setStartTime(LocalDateTime.now());
            subPlan.setExTime(LocalDateTime.now().plusMonths(1));
            subscriptionPlanRepository.save(subPlan);
            response.setCallbackUrl(entity.getCallbackUrl());
            response.setStatus(true);
            response.setOrderId(callback.getOrderId());
            response.setTransactionId(callback.getTransactionId());
        }, () -> {
            System.err.println("Not found subscription payment");
        });;
        return response;
    }

    public void getCurrentSubscriptionAndPayment() {

    }

    private SubscriptionPayment createPayment (SubscriptionOrderRequest request) {
        return SubscriptionPayment.builder()
        .callbackUrl(request.getCallbackUrl())
        .paymentStatus(PaymentStatus.PENDING)
        .totalPayment(BigDecimal.valueOf(request.getSubscriptionRole() == SubscriptionRole.BASIC ? 1000000 : 2900000))
        .exTime(LocalDateTime.now().plusMinutes(15))
        .build();
    }
}
