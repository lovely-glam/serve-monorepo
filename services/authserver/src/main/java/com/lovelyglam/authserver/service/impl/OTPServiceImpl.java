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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.lovelyglam.authserver.service.OTPService;
import com.lovelyglam.database.model.dto.request.OTPVerifyRequest;
import com.lovelyglam.database.model.exception.ActionFailedException;
import com.lovelyglam.database.model.exception.ValidationFailedException;
import com.lovelyglam.database.model.other.OTPKey;
import com.lovelyglam.database.model.other.OTPValue;
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

    public void generateOTPCode(OTPKey key, String username, String other) {
        if (validateOTPGen(key)) {
            var keyJson = convertToJson(key);
            var value = generateRandomOTP();
            var valueJson = OTPValue.builder()
            .otp(value)
            .other(other)
            .build();
            redisTemplate.opsForValue().set(keyJson, valueJson, timeOut, TimeUnit.MINUTES);
            mailSenderService.sendCustomMessage((message) -> {
                try {
                    Context context = new Context();
                    context.setVariable("otp", value);
                    context.setVariable("timeOut", timeOut);
                    context.setVariable("username", username);
                    var content = templateEngine.process("otp-template", context);
                    MimeMessageHelper help = new MimeMessageHelper(message, true);
                    help.setTo(key.getIdentity());
                    help.setSubject("[OTP - LOVELY GLAM]");
                    help.setText(content, true);
                } catch (Exception ex) {
                    redisTemplate.opsForValue().getAndExpire(username, (long)0, TimeUnit.MINUTES);
                    throw new ActionFailedException("Send OTP Failed");
                }
            });
        }
    }

    private boolean validateOTPGen(OTPKey otpKey) {
        var json = convertToJson(otpKey);
        var result = redisTemplate.opsForValue().get(json);
        if (result == null) return true;
        return false;
    }

    public void verifyOTP(OTPVerifyRequest request) {
        var key = convertToJson(OTPKey.builder()
        .identity(request.getIdentity())
        .otpType(request.getType())
        .build()
        );
        var result = (OTPValue) redisTemplate.opsForValue().get(key);
        if (result == null) {
            throw new ValidationFailedException("This OTP is not valid or expiration");
        }
        if (!result.getOtp().equals(request.getOtp())) {
            throw new ValidationFailedException("The OTP code isn't match");
        }
    }

    private String generateRandomOTP() {
        Optional<String> value = Optional.empty();
        SecureRandom random = new SecureRandom();
        Long temp = random.nextLong(maxSize);
        value = Optional.of(String.format("%06d", temp));
        return value.get();
    }

    private <T> String convertToJson(T value) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            var json = ow.writeValueAsString(value);
            return json.trim();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ActionFailedException("Failed to generate OTP Key");
        }
    }
}
