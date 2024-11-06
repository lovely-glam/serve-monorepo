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
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailNotificationService {
    private final NotificationService notificationService;
    private final BookingRepository bookingRepository;

    @Scheduled(cron = "0 0 8 * * ?")
    @Transactional
    public void bookingPrepareNotification() {
        Timestamp tomorrowStart = Timestamp.valueOf(LocalDateTime.now().plusDays(1).with(LocalTime.MIN));
        Timestamp tomorrowEnd = Timestamp.valueOf(LocalDateTime.now().plusDays(1).with(LocalTime.MAX));
        log.debug("Querying for bookings between {} and {}", tomorrowStart, tomorrowEnd);
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
        log.debug("Mapped {} booking appointments for notification.", nextBookDto.size());
        notificationService.prepareMailNotificationBooking(nextBookDto);
    }

    @Scheduled(cron = "0 0 9 * * ?")
    public void pushBookingNotification() {
        log.debug("Starting to push booking notifications for the day...");
        try {
            notificationService.sendMailNotification(new Date());
            log.info("Successfully pushed booking notifications.");

        } catch (Exception ex) {
            log.error("Error while sending booking notifications", ex);
        }
        log.debug("Push booking notification process completed.");
    }
}
