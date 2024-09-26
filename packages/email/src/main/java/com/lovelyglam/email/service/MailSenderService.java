package com.lovelyglam.email.service;

import java.util.List;

import com.lovelyglam.email.model.template.EmailTemplate;
import com.lovelyglam.email.model.template.OTPTemplate;

public interface MailSenderService {
    void sendMessage(EmailTemplate EmailTemplate);
    void sendMultipleMessage(List<EmailTemplate> mailTemplates);
    void sendMultiplePerson(List<String> emails, EmailTemplate template);
    void sendOTP(OTPTemplate template);
}
