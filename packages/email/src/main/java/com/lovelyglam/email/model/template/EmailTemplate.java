package com.lovelyglam.email.model.template;

import lombok.Data;

@Data
public abstract class EmailTemplate {
    private String to;
    private String name;
    private String subject;
    private String text;
}
