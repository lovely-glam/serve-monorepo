package com.lovelyglam.database.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ShopProfileResponse {
    BigDecimal id;
    String name;
    String avatarUrl;
    String thumbnails;
    String address;
    String phone;
}
