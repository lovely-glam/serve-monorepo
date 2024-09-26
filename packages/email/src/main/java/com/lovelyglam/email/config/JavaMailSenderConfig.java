package com.lovelyglam.email.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class JavaMailSenderConfig {
    private final JavaMailInfoConfig javaMailInfoConfig;
    @Bean
    JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(javaMailInfoConfig.getMailHost());
        mailSender.setPort(javaMailInfoConfig.getMailPort());
        
        mailSender.setUsername(javaMailInfoConfig.getMailUsername());
        mailSender.setPassword(javaMailInfoConfig.getMailPassword());
        
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        return mailSender;
    }
}
