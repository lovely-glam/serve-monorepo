package com.lovelyglam.database.model.dto.request;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShopDetailRequest {
    String name;
    String avatarUrl;
    String thumbnails;
    String address;
}
