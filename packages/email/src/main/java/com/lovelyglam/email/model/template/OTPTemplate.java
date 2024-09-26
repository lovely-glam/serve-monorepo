package com.lovelyglam.email.model.template;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OTPTemplate extends EmailTemplate {
    private String otp;
    private String username;
    private String redirectUrl;
}
