package com.lovelyglam.workerservice.cron;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lovelyglam.database.model.entity.Booking;
import com.lovelyglam.database.repository.BookingRepository;
import com.lovelyglam.workerservice.model.BookingAppointment;
import com.lovelyglam.workerservice.service.NotificationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailNotificationService {
    private NotificationService notificationService;
    private final BookingRepository bookingRepository;
    @Scheduled(cron = "0 0 8 * * ?")
    @Transactional
    public void bookingPrepareNotification() {
        Timestamp tomorrowStart = Timestamp.valueOf(LocalDateTime.now().plusDays(1).with(LocalTime.MIN));
        Timestamp tomorrowEnd = Timestamp.valueOf(LocalDateTime.now().plusDays(1).with(LocalTime.MAX));
        List<Booking> nextDayBookings = bookingRepository.findBookingsWithinTimeRange(tomorrowStart, tomorrowEnd);
        var nextBookDto = nextDayBookings.stream().map(i -> {
            var profile = i.getShopService().getShopProfile();
            return BookingAppointment.builder()
            .shopName(profile.getName())
            .location(profile.getAddress())
            .meetDate(i.getStartTime())
            .email(i.getUserAccount().getEmail())
            .build();
        }).toList();
        notificationService.prepareMailNotificationBooking(nextBookDto);
    }
    @Scheduled(cron = "0 0 9 * * ?")
    public void pushBookingNotification() {
        notificationService.sendMailNotification(new Date());
    }
}
