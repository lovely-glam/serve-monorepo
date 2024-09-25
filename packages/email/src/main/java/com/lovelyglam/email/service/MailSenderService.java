package com.lovelyglam.email.service;

import com.lovelyglam.email.model.EmailTemplate;

public interface MailSenderService {
    public void sendMessage(EmailTemplate EmailTemplate);
}
