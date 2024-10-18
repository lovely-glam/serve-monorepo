package com.lovelyglam.database.model.other;

import java.io.Serializable;

import com.lovelyglam.database.model.constant.OTPType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class OTPKey implements Serializable {
    String identity;
    OTPType otpType;
}
