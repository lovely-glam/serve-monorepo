package com.lovelyglam.database.model.dto.response;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NailPostResponse {
    private BigDecimal id;
    private String title;
    private String description;
    private String content;
    private List<String> images;
}
