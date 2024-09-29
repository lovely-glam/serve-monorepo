package com.lovelyglam.utils.payment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class VNPayConfig {
    @Value("${vnp.pay-url}")
    private String vnpPayUrl;
    @Value("${vnp.return-url}")
    private String vnpReturnUrl;
    @Value("${vnp.tmn-code}")
    private String vnpTmnCode;
    @Value("${vnp.hash-secret}")
    private String vnpHashSecret;
    @Value("${vnp.api-url}")
    private String vnpApiUrl;
}