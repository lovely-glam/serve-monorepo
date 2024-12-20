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
import com.lovelyglam.database.model.entity.TransactionId;
import com.lovelyglam.database.model.exception.ActionFailedException;
import com.lovelyglam.database.repository.SubscriptionPaymentRepository;
import com.lovelyglam.database.repository.SubscriptionPlanRepository;
import com.lovelyglam.nailserver.model.SubscriptionPayload;
import com.lovelyglam.nailserver.service.SubscriptionPlanService;
import com.lovelyglam.nailserver.utils.AuthUtils;
import com.lovelyglam.utils.payment.model.PayOSAPICallback;
import com.lovelyglam.utils.payment.model.PaymentAPIRequest;
import com.lovelyglam.utils.payment.model.VNPayApiCallback;
import com.lovelyglam.utils.payment.util.JSONUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final SubscriptionPaymentRepository paymentRepository;
    private final AuthUtils authUtils;

    @Override
    @Transactional(rollbackFor = { ActionFailedException.class })
    public String signSubscriptionPlan(Function<PaymentAPIRequest, String> cb, SubscriptionOrderRequest request) {
        var userProfile = authUtils.getUserAccountFromAuthentication();
        var subscription = userProfile.getSubscriptionPlans();
        var currentTime = LocalDateTime.now();
        var subPayload = SubscriptionPayload.builder()
                .newSubscriptionRole(request.getSubscriptionRole())
                .build();
        if (subscription == null) {
            subscription = SubscriptionPlan.builder()
                    .role(SubscriptionRole.NONE)
                    .shopAccount(userProfile)
                    .status(SubscriptionPlanStatus.PENDING)
                    .build();
            subPayload.setAction("CREATE");
            subPayload.setCurrentSubscriptionRole(SubscriptionRole.NONE);
            subPayload.setNewSubscriptionRole(request.getSubscriptionRole());
        } else if (subscription.getExTime() != null
                && (currentTime.isAfter(subscription.getExTime()) || currentTime.isBefore(subscription.getExTime()))) {
            subPayload.setAction("EXTEND");
            subPayload.setCurrentSubscriptionRole(subscription.getRole());
            subPayload.setNewSubscriptionRole(request.getSubscriptionRole());
        }
        try {
            var paymentEntity = createPayment(request);
            var subscriptionResult = subscriptionPlanRepository.save(subscription);
            paymentEntity.setSubscriptionPlan(subscriptionResult);
            String payload = JSONUtil.toJson(subPayload);
            paymentEntity.setPayload(payload);
            var paymentResult = paymentRepository.save(paymentEntity);
            var paymentApiRequest = PaymentAPIRequest.builder()
                    .callBackUrl(request.getCallbackUrl())
                    .orderId(paymentResult.getTransactionId().getId().toBigInteger().intValue())
                    .totalAmount(paymentResult.getTotalPayment().intValue())
                    .orderInfo(String.format("PAYMENT FOR SUBSCRIPTION %s", subscription.getRole().toString()))
                    .build();
            return cb.apply(paymentApiRequest);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ActionFailedException("Failed to add subscription", ex);
        }
    }

    public PaymentSubscriptionResponse activeSubscription(VNPayApiCallback callback) {
        BigDecimal orderId = new BigDecimal(callback.getOrderId());
        PaymentSubscriptionResponse response = new PaymentSubscriptionResponse();
        paymentRepository.findByTransactionId(orderId).ifPresentOrElse((entity) -> {
            if (callback.getPaymentStatus() == true) {
                entity.setPaymentStatus(PaymentStatus.COMPLETED);
            } else {
                entity.setPaymentStatus(PaymentStatus.FAILED);
            }
            paymentRepository.save(entity);
            String payload = entity.getPayload();
            var subPlan = entity.getSubscriptionPlan();
            if (callback.getPaymentStatus() == true) {
                updateSubscriptionPlan(subPlan, payload);
            }
            response.setCallbackUrl(entity.getCallbackUrl());
            response.setStatus(callback.getPaymentStatus());
            response.setOrderId(callback.getOrderId());
            response.setTransactionId(callback.getTransactionId());
        }, () -> {
            System.err.println("Not found subscription payment");
            throw new ActionFailedException("Can't Activate Subscription: Not found subscription payment");
        });
        ;
        return response;
    }

    public void updateSubscriptionPlan(SubscriptionPlan subPlan, String payload) {
        SubscriptionPayload payloadObject = JSONUtil.fromJson(payload, SubscriptionPayload.class);
        if (payloadObject.getAction().equals("CREATE")) {
            subPlan.setRole(payloadObject.getNewSubscriptionRole());
            subPlan.setExTime(LocalDateTime.now().plusMonths(1));
            subPlan.setStartTime(LocalDateTime.now());
        } else if (payloadObject.getAction().equals("EXTEND")) {
            if (subPlan.getExTime().isBefore(LocalDateTime.now())) {
                subPlan.setExTime(subPlan.getExTime().plusMonths(1));
            } else {
                subPlan.setExTime(LocalDateTime.now().plusMonths(1));
            }
        } else if (payloadObject.getAction().equals("CHANGE")
                && subPlan.getRole() == payloadObject.getCurrentSubscriptionRole()) {
            subPlan.setRole(payloadObject.getNewSubscriptionRole());
            subPlan.setExTime(subPlan.getExTime().plusMonths(1));
        }
        subPlan.setStatus(SubscriptionPlanStatus.ACTIVE);
        subscriptionPlanRepository.save(subPlan);
    }

    private SubscriptionPayment createPayment(SubscriptionOrderRequest request) {
        return SubscriptionPayment.builder()
                .callbackUrl(request.getCallbackUrl())
                .paymentStatus(PaymentStatus.PENDING)
                .totalPayment(
                        BigDecimal.valueOf(request.getSubscriptionRole() == SubscriptionRole.BASIC ? 1000000 : 2500000))
                .exTime(LocalDateTime.now().plusMinutes(15))
                .transactionId(TransactionId.builder()
                        .merchantType(request.getType())
                        .build())
                .build();
    }

    @Override
    public PaymentSubscriptionResponse activeSubscription(PayOSAPICallback callback) {
        BigDecimal orderId = new BigDecimal(callback.getOrderId());
        PaymentSubscriptionResponse response = new PaymentSubscriptionResponse();
        paymentRepository.findByTransactionId(orderId).ifPresentOrElse((entity) -> {
            if (callback.getPaymentStatus() == true) {
                entity.setPaymentStatus(PaymentStatus.COMPLETED);
            } else {
                entity.setPaymentStatus(PaymentStatus.FAILED);
            }
            paymentRepository.save(entity);
            String payload = entity.getPayload();
            var subPlan = entity.getSubscriptionPlan();
            if (callback.getPaymentStatus() == true) {
                updateSubscriptionPlan(subPlan, payload);
            }
            response.setCallbackUrl(entity.getCallbackUrl());
            response.setStatus(callback.getPaymentStatus());
            response.setOrderId(callback.getOrderId());
            response.setTransactionId(callback.getTransactionId());
        }, () -> {
            System.err.println("Not found subscription payment");
            throw new ActionFailedException("Can't Activate Subscription: Not found subscription payment");
        });
        ;
        return response;
    }
}
