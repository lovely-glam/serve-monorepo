package com.lovelyglam.database.model.dto.request;


import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShopDetailRequest {
    String name;
    String avatarUrl;
    List<String> thumbnails;
    String address;
}
