package com.lovelyglam.workerservice.service;

import java.util.Date;
import java.util.List;

import com.lovelyglam.workerservice.model.BookingAppointment;

public interface NotificationService {
    public void prepareMailNotificationBooking(List<BookingAppointment> bookings);
    void sendMailNotification(Date key);
}
