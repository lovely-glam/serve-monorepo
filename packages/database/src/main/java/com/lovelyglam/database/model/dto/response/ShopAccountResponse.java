package com.lovelyglam.database.model.dto.response;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShopAccountResponse {
    private BigDecimal id;
    private String username;
    private String email;
    private boolean isActive;
    private boolean isVerified;
}
