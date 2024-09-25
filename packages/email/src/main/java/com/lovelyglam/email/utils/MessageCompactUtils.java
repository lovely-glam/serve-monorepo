package com.lovelyglam.email.utils;

import java.util.List;
import java.util.function.Function;

import com.lovelyglam.email.model.EmailTemplate;

import jakarta.mail.internet.MimeMessage;

public class MessageCompactUtils {
    public static List<MimeMessage> convertTemplateToMessage(
        List<EmailTemplate> template, 
        Function<EmailTemplate,MimeMessage> mapper
    ) {
        var result = template.stream()
        .map((a) -> {
            return mapper.apply(a);
        }).toList();
        return result.stream().filter((item) -> item != null).toList();
    }
}
