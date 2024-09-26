package com.lovelyglam.authserver.service.impl;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.lovelyglam.authserver.service.OTPService;
import com.lovelyglam.database.model.dto.request.OTPVerifyRequest;
import com.lovelyglam.database.model.exception.ActionFailedException;
import com.lovelyglam.database.model.exception.ValidationFailedException;
import com.lovelyglam.email.service.MailSenderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OTPServiceImpl implements OTPService {
    private final MailSenderService mailSenderService;
    private final TemplateEngine templateEngine;
    @Value("${otp.max-size}")
    private Long maxSize;
    @Value("${otp.timeout}")
    private Long timeOut;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void generateOTPCode(String identity, String username) {
        var value = generateRandomOTP();
        redisTemplate.opsForValue().set(value, identity, 10, TimeUnit.MINUTES);
        mailSenderService.sendCustomMessage((message) -> {
            try {
                Context context = new Context();
                context.setVariable("otp", value);
                context.setVariable("timeOut", timeOut);
                context.setVariable("username", username);
                var content = templateEngine.process("otp-template", context);
                MimeMessageHelper help = new MimeMessageHelper(message, true);
                help.setTo(identity);
                help.setSubject("[OTP - LOVELY GLAM]");
                help.setText(content, true);
            } catch (Exception ex) {
                redisTemplate.opsForValue().getAndExpire(username, (long)0, TimeUnit.MINUTES);
                throw new ActionFailedException("Send OTP Failed");
            }
        });
    }

    public void verifyOTP(OTPVerifyRequest request) {
        var result = (String) redisTemplate.opsForValue().get(request.getOtp());
        if (result == null) {
            throw new ValidationFailedException("This OTP is not valid or expiration");
        }
        if (!result.equals(request.getIdentity())) {
            throw new ValidationFailedException("The identity isn't match");
        }
    }

    private String generateRandomOTP() {
        Optional<String> value = Optional.empty();
        do {
            SecureRandom random = new SecureRandom();
            Long temp = random.nextLong(maxSize);
            value = Optional.of(String.format("%06d", temp));
        } while (redisTemplate.opsForValue().get(value.get()) != null);  
        return value.get();
    }

    @Override
    public String verifyOTP(String otp) {
        var result = (String) redisTemplate.opsForValue().get(otp);
        if (result == null) {
            throw new ValidationFailedException("This OTP is not valid or expiration");
        }
        return result;
    }
}
