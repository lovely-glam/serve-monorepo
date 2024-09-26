package com.lovelyglam.authserver.service.impl;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.lovelyglam.authserver.service.OTPService;
import com.lovelyglam.database.model.dto.request.OTPVerifyRequest;
import com.lovelyglam.database.model.exception.ValidationFailedException;
import com.lovelyglam.database.repository.ShopAccountRepository;
import com.lovelyglam.database.repository.UserAccountRepository;
import com.lovelyglam.email.service.MailSenderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OTPServiceImpl implements OTPService {
    private final UserAccountRepository userAccountRepository;
    private final ShopAccountRepository shopAccountRepository;
    private final MailSenderService mailSenderService;
    @Value("${otp.max-size}")
    private Long maxSize;
    @Value("${otp.timeout}")
    private Long timeOut;
    private final RedisTemplate<Long, Object> redisTemplate;

    public Long generateOTPCode(String identity) {
        var value = generateRandomOTP();
        redisTemplate.opsForValue().set(value, identity, 10, TimeUnit.MINUTES);
        
        return value;
    }

    public void verifyOTP(OTPVerifyRequest request) {
        var result = (String) redisTemplate.opsForValue().get(request.getOtp());
        if (result == null) {
            throw new ValidationFailedException("This OTP is not valid or expiration");
        }
    }

    private Long generateRandomOTP() {
        Optional<Long> value = Optional.empty();
        do {
            SecureRandom random = new SecureRandom();
            value = Optional.of(random.nextLong(maxSize));
        } while (redisTemplate.opsForValue().get(value.get()) == null);  
        return value.get();
    }
}
