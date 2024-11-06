package com.lovelyglam.workerservice.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.lovelyglam.email.service.MailSenderService;
import com.lovelyglam.workerservice.model.BookingAppointment;
import com.lovelyglam.workerservice.service.NotificationService;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final MailSenderService mailSenderService;
    private final TemplateEngine templateEngine;
    private final RedisTemplate<Date, List<BookingAppointment>> redisBookingQueueTemplate;
    @Override
    public void prepareMailNotificationBooking(List<BookingAppointment> bookings) {
        if(bookings == null) {
            bookings = new ArrayList<>();
        }
        redisBookingQueueTemplate.opsForValue().set(new Date(), bookings); 
    }

    public void sendMailNotification(Date key) {
        var list = redisBookingQueueTemplate.opsForValue().get(key);
        if (list == null) {
            return;
        }
        mailSenderService.sendBulkCustomMessage(mailSender -> {
            List<MimeMessage> listMessage = new ArrayList<>();
            list.forEach(item -> {
                var temp = mailSender.createMimeMessage();
                Context context = new Context();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                String formattedDate = dateFormat.format(item.getMeetDate());
                String formattedTime = timeFormat.format(item.getMeetDate());
                context.setVariable("eventName", item.getShopName());
                context.setVariable("eventDate", formattedDate);
                context.setVariable("eventTime", formattedTime);
                context.setVariable("username", item.getEmail());
                context.setVariable("eventLocation", item.getLocation());
                try {
                    var content = templateEngine.process("booking-date", context);
                    MimeMessageHelper helper = new MimeMessageHelper(temp, true);
                    helper.setTo(item.getEmail());
                    helper.setText(content,true);
                    listMessage.add(temp);
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            });
            return listMessage;
        });
        redisBookingQueueTemplate.delete(key);
    }
    
}
