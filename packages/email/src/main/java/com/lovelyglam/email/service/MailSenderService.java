package com.lovelyglam.email.service;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.mail.javamail.JavaMailSender;

import com.lovelyglam.email.model.template.EmailTemplate;

import jakarta.mail.internet.MimeMessage;

public interface MailSenderService {
    void sendMessage(EmailTemplate EmailTemplate);
    void sendMultipleMessage(List<EmailTemplate> mailTemplates);
    void sendMultiplePerson(List<String> emails, EmailTemplate template);
    void sendCustomMessage(Consumer<MimeMessage> callback);
    void sendBulkCustomMessage(Function<JavaMailSender,List<MimeMessage>> callback);
}
