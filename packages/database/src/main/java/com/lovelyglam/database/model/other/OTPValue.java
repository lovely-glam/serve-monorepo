package com.lovelyglam.database.model.other;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class OTPValue implements Serializable {
    String otp;
    String other;
}
