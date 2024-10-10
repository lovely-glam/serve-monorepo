package com.lovelyglam.workerservice.cron;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lovelyglam.database.model.constant.PaymentStatus;
import com.lovelyglam.database.repository.SubscriptionPaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubscriptionPaymentTrackingService {
    private final SubscriptionPaymentRepository subscriptionPaymentRepository;

    @Scheduled(fixedDelay = 60000)
    @Transactional(rollbackFor = Exception.class)
    public void updateExpiredPayments() {
        LocalDateTime now = LocalDateTime.now();
        
        int updatedCount = subscriptionPaymentRepository.updateExpiredPayments(now, PaymentStatus.FAILED, PaymentStatus.PENDING);
        
        System.out.println("Updated " + updatedCount + " expired subscription payments to TIMEOUT status.");
    }
}
