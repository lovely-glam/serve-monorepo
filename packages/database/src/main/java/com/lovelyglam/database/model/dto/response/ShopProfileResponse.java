package com.lovelyglam.database.model.dto.response;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShopProfileResponse {
    BigDecimal id;
    String name;
    String avatarUrl;
    List<String> thumbnails;
    String address;
    String phone;
    List<NailServiceResponse> nailServices;
    Double vote;
}
