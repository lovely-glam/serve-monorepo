package com.lovelyglam.email.service.impl;

import java.util.List;
import java.util.function.Consumer;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.lovelyglam.email.model.template.EmailTemplate;
import com.lovelyglam.email.service.MailSenderService;
import com.lovelyglam.email.utils.MessageCompactUtils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailSenderService {
    private final JavaMailSender emailSender;
    private String sender = "noreply@lovelyglam.life";
    @Override
    public void sendMessage(EmailTemplate emailTemplate) {
        MimeMessage message = emailSender.createMimeMessage();
        
        try {
            MimeMessageHelper help = new MimeMessageHelper(message, true);
            help = new MimeMessageHelper(message, true);
            help.setFrom("noreply@lovelyglam.life");
            help.setTo(emailTemplate.getTo());
            help.setSubject(emailTemplate.getSubject());
            help.setText(emailTemplate.getText());
        } catch (MessagingException e) {
        }
        emailSender.send(message);
    }

    public void sendMultipleMessage(List<EmailTemplate> mailTemplates) {
        List<MimeMessage> list = MessageCompactUtils.convertTemplateToMessage(mailTemplates, (template) -> {
            try {
                MimeMessage message = emailSender.createMimeMessage();
                MimeMessageHelper help = new MimeMessageHelper(message, true);
                help.setFrom(sender);
                help.setTo(template.getTo());
                help.setSubject(template.getSubject());
                help.setText(template.getText());
                return message;
            } catch (Exception ex) {
                return null;
            }
        });
        emailSender.send(list.toArray(new MimeMessage[0]));
    }

    public void sendMultiplePerson(List<String> emails, EmailTemplate template) {
        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper help = new MimeMessageHelper(message, true);
            help.setFrom(sender);
            help.setTo(emails.toArray(new String[0]));
            help.setSubject(sender);
            help.setText(template.getText());
        } catch (Exception ex) {
        }
        emailSender.send(message);
    }

    
    public void sendCustomMessage(Consumer<MimeMessage> callback) {
        MimeMessage message = emailSender.createMimeMessage();
        callback.accept(message);
        try {
            message.setFrom("noreply@lovelyglam.life");
        }catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        emailSender.send(message);
    }
    
    
}
