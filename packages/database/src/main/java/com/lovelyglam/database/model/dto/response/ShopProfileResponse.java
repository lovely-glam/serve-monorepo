package com.lovelyglam.database.model.dto.response;

import com.lovelyglam.database.model.entity.ShopService;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

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
