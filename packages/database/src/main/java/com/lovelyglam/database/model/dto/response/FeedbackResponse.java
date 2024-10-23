package com.lovelyglam.database.model.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeedbackResponse {
    private String id;
    private BigDecimal shopId;
    private String shopAvatar;
    private String shopName;
    private String location;
    private Double rating;
    private Integer reviewNumber;
    private String customerName;
    private String userAvatar;
    private String service;
    private LocalDateTime joinDate;
    private String feedback;
}
