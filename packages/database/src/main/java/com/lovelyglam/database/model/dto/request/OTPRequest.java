package com.lovelyglam.database.model.dto.request;

import com.lovelyglam.database.model.constant.OTPType;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OTPRequest {
    private String identity;
    private OTPType type;
}
