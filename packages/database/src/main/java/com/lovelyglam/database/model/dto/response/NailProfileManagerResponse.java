package com.lovelyglam.database.model.dto.response;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NailProfileManagerResponse {
    private BigDecimal id;
    private String avatarUrl;
    private String name;
    private Double averageVote;
    private Integer totalVote;
    private String address;
    private BigDecimal profit;
}
