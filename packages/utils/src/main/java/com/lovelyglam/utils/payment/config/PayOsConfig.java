package com.lovelyglam.utils.payment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import vn.payos.PayOS;

@Getter
@Configuration
public class PayOsConfig {
    @Value("${payos.client-id}")
    private String clientId;
    @Value("${payos.api-key}")
    private String apiKey;
    @Value("${payos.checksum-key}")
    private String checksumKey;

    @Bean
	PayOS payOS() {
		return new PayOS(clientId, apiKey, checksumKey);
	}
}
