package com.lovelyglam.workerservice.cron;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lovelyglam.database.model.constant.PaymentStatus;
import com.lovelyglam.database.repository.BookingPaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingPaymentTrackingStatusService {
    private final BookingPaymentRepository bookingPaymentRepository;

    @Scheduled(fixedRate = 60000)
    @Transactional(rollbackFor = Exception.class)
    public void updatePaymentStatus() {
        LocalDateTime now = LocalDateTime.now();
        bookingPaymentRepository.updatePaymentStatusToTimeout(PaymentStatus.CANCELED, PaymentStatus.PENDING, now);
        
        System.out.println("Updated all expired payment statuses to TIMEOUT.");
    }
}
